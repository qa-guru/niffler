package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendshipEntity;
import guru.qa.niffler.data.FriendshipStatus;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.ex.SameUsernameException;
import guru.qa.niffler.model.UserJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static guru.qa.niffler.model.FriendState.FRIEND;
import static guru.qa.niffler.model.FriendState.INVITE_RECEIVED;
import static guru.qa.niffler.model.FriendState.INVITE_SENT;

@Component
public class UserDataService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataService.class);

    private static final CurrencyValues DEFAULT_USER_CURRENCY = CurrencyValues.RUB;
    private final UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
        LOG.info("### Kafka consumer record: " + cr.toString());
        UserEntity userDataEntity = new UserEntity();
        userDataEntity.setUsername(user.username());
        userDataEntity.setCurrency(DEFAULT_USER_CURRENCY);
        UserEntity userEntity = userRepository.save(userDataEntity);
        LOG.info(String.format(
                "### User '%s' successfully saved to database with id: %s",
                user.username(),
                userEntity.getId()
        ));
    }

    @Transactional
    public @Nonnull
    UserJson update(@Nonnull UserJson user) {
        UserEntity userEntity = getRequiredUser(user.username());
        userEntity.setFirstname(user.firstname());
        userEntity.setSurname(user.surname());
        userEntity.setCurrency(user.currency());
        userEntity.setPhoto(user.photo() != null ? user.photo().getBytes(StandardCharsets.UTF_8) : null);
        UserEntity saved = userRepository.save(userEntity);
        return UserJson.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public @Nonnull
    UserJson getCurrentUser(@Nonnull String username) {
        return UserJson.fromEntity(getRequiredUser(username));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username,
                            @Nullable String searchQuery) {
        List<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findByUsernameNot(username)
                : userRepository.findByUsernameNot(username, searchQuery);

        return usersFromDb.stream()
                .map(ue -> mapToUserJsonWithFriendshipState(username, ue))
                .toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<UserJson> allUsers(@Nonnull String username,
                            @Nonnull Pageable pageable,
                            @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findByUsernameNot(username, pageable)
                : userRepository.findByUsernameNot(username, searchQuery, pageable);

        return usersFromDb.map(ue -> mapToUserJsonWithFriendshipState(username, ue));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> friends(@Nonnull String username,
                           @Nullable String searchQuery) {
        List<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findFriends(getRequiredUser(username))
                : userRepository.findFriends(getRequiredUser(username), searchQuery);

        return usersFromDb.stream().map(f -> UserJson.fromEntity(f, FRIEND)).toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<UserJson> friends(@Nonnull String username,
                           @Nonnull Pageable pageable,
                           @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findFriends(getRequiredUser(username), pageable)
                : userRepository.findFriends(getRequiredUser(username), searchQuery, pageable);

        return usersFromDb.map(f -> UserJson.fromEntity(f, FRIEND));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> incomeInvitations(@Nonnull String username,
                                     @Nullable String searchQuery) {
        List<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findIncomeInvitations(getRequiredUser(username))
                : userRepository.findIncomeInvitations(getRequiredUser(username), searchQuery);

        return usersFromDb.stream().map(i -> UserJson.fromEntity(i, INVITE_RECEIVED)).toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<UserJson> incomeInvitations(@Nonnull String username,
                                     @Nonnull Pageable pageable,
                                     @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findIncomeInvitations(getRequiredUser(username), pageable)
                : userRepository.findIncomeInvitations(getRequiredUser(username), searchQuery, pageable);

        return usersFromDb.map(i -> UserJson.fromEntity(i, INVITE_RECEIVED));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJson> outcomeInvitations(@Nonnull String username,
                                      @Nullable String searchQuery) {
        List<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findOutcomeInvitations(getRequiredUser(username))
                : userRepository.findOutcomeInvitations(getRequiredUser(username), searchQuery);

        return usersFromDb.stream().map(i -> UserJson.fromEntity(i, INVITE_SENT)).toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<UserJson> outcomeInvitations(@Nonnull String username,
                                      @Nonnull Pageable pageable,
                                      @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findOutcomeInvitations(getRequiredUser(username), pageable)
                : userRepository.findOutcomeInvitations(getRequiredUser(username), searchQuery, pageable);

        return usersFromDb.map(i -> UserJson.fromEntity(i, INVITE_SENT));
    }

    @Transactional
    public UserJson createFriendshipRequest(@Nonnull String username, @Nonnull String targetUsername) {
        if (Objects.equals(username, targetUsername)) {
            throw new SameUsernameException("Can`t create friendship request for self user");
        }
        UserEntity currentUser = getRequiredUser(username);
        UserEntity targetUser = getRequiredUser(targetUsername);
        currentUser.addFriends(FriendshipStatus.PENDING, targetUser);
        userRepository.save(currentUser);
        return UserJson.fromEntity(targetUser, INVITE_SENT);
    }

    @Transactional
    public @Nonnull
    UserJson acceptFriendshipRequest(@Nonnull String username, @Nonnull String targetUsername) {
        if (Objects.equals(username, targetUsername)) {
            throw new SameUsernameException("Can`t accept friendship request for self user");
        }
        UserEntity currentUser = getRequiredUser(username);
        UserEntity targetUser = getRequiredUser(targetUsername);

        FriendshipEntity invite = currentUser.getFriendshipAddressees()
                .stream()
                .filter(fe -> fe.getRequester().getUsername().equals(targetUser.getUsername()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Can`t find invitation from username: '" + targetUsername + "'"));

        invite.setStatus(FriendshipStatus.ACCEPTED);
        currentUser.addFriends(FriendshipStatus.ACCEPTED, targetUser);
        userRepository.save(currentUser);
        return UserJson.fromEntity(targetUser, FRIEND);
    }

    @Transactional
    public @Nonnull
    UserJson declineFriendshipRequest(@Nonnull String username, @Nonnull String targetUsername) {
        if (Objects.equals(username, targetUsername)) {
            throw new SameUsernameException("Can`t decline friendship request for self user");
        }
        UserEntity currentUser = getRequiredUser(username);
        UserEntity targetUser = getRequiredUser(targetUsername);

        currentUser.removeInvites(targetUser);
        targetUser.removeFriends(currentUser);

        userRepository.save(currentUser);
        userRepository.save(targetUser);
        return UserJson.fromEntity(targetUser);
    }

    @Transactional
    public void removeFriend(@Nonnull String username, @Nonnull String targetUsername) {
        if (Objects.equals(username, targetUsername)) {
            throw new SameUsernameException("Can`t remove friendship relation for self user");
        }
        UserEntity currentUser = getRequiredUser(username);
        UserEntity targetUser = getRequiredUser(targetUsername);

        currentUser.removeFriends(targetUser);
        currentUser.removeInvites(targetUser);
        targetUser.removeFriends(currentUser);
        targetUser.removeInvites(currentUser);

        userRepository.save(currentUser);
        userRepository.save(targetUser);
    }

    @Nonnull
    UserEntity getRequiredUser(@Nonnull String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Can`t find user by username: '" + username + "'")
        );
    }

    @Nonnull
    UserJson mapToUserJsonWithFriendshipState(@Nonnull String username,
                                              @Nonnull UserEntity userEntity) {
        List<FriendshipEntity> requests = userEntity.getFriendshipRequests();
        List<FriendshipEntity> addresses = userEntity.getFriendshipAddressees();

        if (!requests.isEmpty()) {
            return requests.stream()
                    .filter(i -> i.getAddressee().getUsername().equals(username))
                    .findFirst()
                    .map(
                            itm -> UserJson.fromEntity(userEntity, itm.getStatus() == FriendshipStatus.PENDING
                                    ? INVITE_RECEIVED
                                    : FRIEND)
                    ).orElse(UserJson.fromEntity(userEntity));
        }
        if (!addresses.isEmpty()) {
            return addresses.stream()
                    .filter(i -> i.getRequester().getUsername().equals(username))
                    .findFirst()
                    .map(
                            itm -> UserJson.fromEntity(userEntity, itm.getStatus() == FriendshipStatus.PENDING
                                    ? INVITE_SENT
                                    : FRIEND)
                    ).orElse(UserJson.fromEntity(userEntity));
        }
        return UserJson.fromEntity(userEntity);
    }
}
