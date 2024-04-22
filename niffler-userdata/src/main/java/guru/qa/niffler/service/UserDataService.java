package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendshipEntity;
import guru.qa.niffler.data.FriendshipStatus;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.ex.SameUsernameException;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserJsonBulk;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
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

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    /**
     * only for migration V4__small_avatar.sql
     */
    @Transactional
    @PostConstruct
    public void compressAndSaveExistingPhotos() {
        List<UserEntity> users = userRepository.findAll();
        for (UserEntity user : users) {
            if ((user.getPhoto() != null && user.getPhoto().length > 0)
                    && (user.getPhotoSmall() == null || user.getPhotoSmall().length == 0)) {
                try {
                    String originalPhoto = new String(user.getPhoto(), StandardCharsets.UTF_8);
                    user.setPhotoSmall(resizePhoto(originalPhoto, user.getId()));
                    userRepository.save(user);
                    LOG.info("### Resizing original user Photo for user done: {}", user.getId());
                } catch (Exception e) {
                    LOG.error("### Error while resizing original user Photo for user :{}", user.getId());
                }
            }
        }
    }

    @Transactional
    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
        LOG.info("### Kafka consumer record: {}", cr.toString());
        UserEntity userDataEntity = new UserEntity();
        userDataEntity.setUsername(user.username());
        userDataEntity.setCurrency(DEFAULT_USER_CURRENCY);
        UserEntity userEntity = userRepository.save(userDataEntity);
        LOG.info(
                "### User '{}' successfully saved to database with id: {}",
                user.username(),
                userEntity.getId()
        );
    }

    @Transactional
    public @Nonnull
    UserJson update(@Nonnull UserJson user) {
        UserEntity userEntity = getRequiredUser(user.username());
        userEntity.setFirstname(user.firstname());
        userEntity.setSurname(user.surname());
        userEntity.setCurrency(user.currency());
        userEntity.setPhoto(user.photo() != null ? user.photo().getBytes(StandardCharsets.UTF_8) : null);
        userEntity.setPhotoSmall(resizePhoto(user.photo(), userEntity.getId()));
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
    List<UserJsonBulk> allUsers(@Nonnull String username,
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
    Page<UserJsonBulk> allUsers(@Nonnull String username,
                                @Nonnull Pageable pageable,
                                @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findByUsernameNot(username, pageable)
                : userRepository.findByUsernameNot(username, searchQuery, pageable);

        return usersFromDb.map(ue -> mapToUserJsonWithFriendshipState(username, ue));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJsonBulk> friends(@Nonnull String username,
                               @Nullable String searchQuery) {
        List<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findFriends(getRequiredUser(username))
                : userRepository.findFriends(getRequiredUser(username), searchQuery);

        return usersFromDb.stream().map(f -> UserJsonBulk.fromEntity(f, FRIEND)).toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<UserJsonBulk> friends(@Nonnull String username,
                               @Nonnull Pageable pageable,
                               @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findFriends(getRequiredUser(username), pageable)
                : userRepository.findFriends(getRequiredUser(username), searchQuery, pageable);

        return usersFromDb.map(f -> UserJsonBulk.fromEntity(f, FRIEND));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJsonBulk> incomeInvitations(@Nonnull String username,
                                         @Nullable String searchQuery) {
        List<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findIncomeInvitations(getRequiredUser(username))
                : userRepository.findIncomeInvitations(getRequiredUser(username), searchQuery);

        return usersFromDb.stream().map(i -> UserJsonBulk.fromEntity(i, INVITE_RECEIVED)).toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<UserJsonBulk> incomeInvitations(@Nonnull String username,
                                         @Nonnull Pageable pageable,
                                         @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findIncomeInvitations(getRequiredUser(username), pageable)
                : userRepository.findIncomeInvitations(getRequiredUser(username), searchQuery, pageable);

        return usersFromDb.map(i -> UserJsonBulk.fromEntity(i, INVITE_RECEIVED));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<UserJsonBulk> outcomeInvitations(@Nonnull String username,
                                          @Nullable String searchQuery) {
        List<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findOutcomeInvitations(getRequiredUser(username))
                : userRepository.findOutcomeInvitations(getRequiredUser(username), searchQuery);

        return usersFromDb.stream().map(i -> UserJsonBulk.fromEntity(i, INVITE_SENT)).toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<UserJsonBulk> outcomeInvitations(@Nonnull String username,
                                          @Nonnull Pageable pageable,
                                          @Nullable String searchQuery) {
        Page<UserEntity> usersFromDb = searchQuery == null
                ? userRepository.findOutcomeInvitations(getRequiredUser(username), pageable)
                : userRepository.findOutcomeInvitations(getRequiredUser(username), searchQuery, pageable);

        return usersFromDb.map(i -> UserJsonBulk.fromEntity(i, INVITE_SENT));
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
    UserJsonBulk mapToUserJsonWithFriendshipState(@Nonnull String username,
                                                  @Nonnull UserEntity userEntity) {
        List<FriendshipEntity> requests = userEntity.getFriendshipRequests();
        List<FriendshipEntity> addresses = userEntity.getFriendshipAddressees();

        if (!requests.isEmpty()) {
            return requests.stream()
                    .filter(i -> i.getAddressee().getUsername().equals(username))
                    .findFirst()
                    .map(
                            itm -> UserJsonBulk.fromEntity(userEntity, itm.getStatus() == FriendshipStatus.PENDING
                                    ? INVITE_RECEIVED
                                    : FRIEND)
                    ).orElse(UserJsonBulk.fromEntity(userEntity));
        }
        if (!addresses.isEmpty()) {
            return addresses.stream()
                    .filter(i -> i.getRequester().getUsername().equals(username))
                    .findFirst()
                    .map(
                            itm -> UserJsonBulk.fromEntity(userEntity, itm.getStatus() == FriendshipStatus.PENDING
                                    ? INVITE_SENT
                                    : FRIEND)
                    ).orElse(UserJsonBulk.fromEntity(userEntity));
        }
        return UserJsonBulk.fromEntity(userEntity);
    }

    private @Nullable byte[] resizePhoto(@Nullable String photo, @Nonnull UUID userId) {
        if (photo != null) {
            try {
                String base64Image = photo.split(",")[1];

                try (ByteArrayInputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(base64Image));
                     ByteArrayOutputStream os = new ByteArrayOutputStream()) {

                    Thumbnails.of(ImageIO.read(is))
                            .height(100)
                            .width(100)
                            .outputQuality(1.0)
                            .outputFormat("png")
                            .toOutputStream(os);

                    return concatArrays(
                            "data:image/png;base64,".getBytes(StandardCharsets.UTF_8),
                            Base64.getEncoder().encode(os.toByteArray())
                    );
                }
            } catch (Exception e) {
                LOG.error("### Error while resizing photo for user: {}", userId);
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private @Nonnull byte[] concatArrays(@Nonnull byte[] first, @Nonnull byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
