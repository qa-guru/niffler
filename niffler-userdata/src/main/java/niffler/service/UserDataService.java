package niffler.service;

import jakarta.annotation.Nonnull;
import niffler.data.CurrencyValues;
import niffler.data.FriendsEntity;
import niffler.data.UserEntity;
import niffler.data.repository.UserRepository;
import niffler.model.FriendJson;
import niffler.model.FriendState;
import niffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDataService {

    private static final CurrencyValues DEFAULT_USER_CURRENCY = CurrencyValues.RUB;
    private final UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @Nonnull
    UserJson update(@Nonnull UserJson user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUserName());
        userEntity.setFirstname(user.getFirstname());
        userEntity.setSurname(user.getSurname());
        userEntity.setCurrency(user.getCurrency());
        userEntity.setPhoto(user.getPhoto() != null ? user.getPhoto().getBytes(StandardCharsets.UTF_8) : null);
        UserEntity saved = userRepository.save(userEntity);

        return UserJson.fromEntity(saved);
    }

    public @Nonnull
    UserJson getCurrentUserOrCreateIfAbsent(@Nonnull String username) {
        UserEntity userDataEntity = userRepository.findByUsername(username);
        if (userDataEntity == null) {
            userDataEntity = new UserEntity();
            userDataEntity.setUsername(username);
            userDataEntity.setCurrency(DEFAULT_USER_CURRENCY);
            return UserJson.fromEntity(userRepository.save(userDataEntity));
        } else {
            return UserJson.fromEntity(userDataEntity);
        }
    }

    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username) {
        Map<UUID, UserJson> result = new HashMap<>();
        for (UserEntity user : userRepository.findByUsernameNot(username)) {
            List<FriendsEntity> sendInvites = user.getFriends();
            List<FriendsEntity> receivedInvites = user.getInvites();

            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
                Optional<FriendsEntity> inviteToMe = sendInvites.stream()
                        .filter(i -> i.getFriend().getUsername().equals(username))
                        .findFirst();

                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream()
                        .filter(i -> i.getUser().getUsername().equals(username))
                        .findFirst();

                if (inviteToMe.isPresent()) {
                    FriendsEntity invite = inviteToMe.get();
                    result.put(user.getId(), UserJson.fromEntity(user, invite.isPending()
                            ? FriendState.INVITE_RECEIVED
                            : FriendState.FRIEND));
                }
                if (inviteFromMe.isPresent()) {
                    FriendsEntity invite = inviteFromMe.get();
                    result.put(user.getId(), UserJson.fromEntity(user, invite.isPending()
                            ? FriendState.INVITE_SENT
                            : FriendState.FRIEND));
                }
            }
            if (!result.containsKey(user.getId())) {
                result.put(user.getId(), UserJson.fromEntity(user));
            }
        }
        return new ArrayList<>(result.values());
    }

    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        return userRepository.findByUsername(username)
                .getFriends()
                .stream()
                .filter(fe -> includePending || !fe.isPending())
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
                        ? FriendState.INVITE_SENT
                        : FriendState.FRIEND))
                .toList();
    }

    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        return userRepository.findByUsername(username)
                .getInvites()
                .stream()
                .filter(FriendsEntity::isPending)
                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendState.INVITE_RECEIVED))
                .toList();
    }

    public void addFriend(@Nonnull String username, @Nonnull FriendJson friend) {
        UserEntity currentUser = userRepository.findByUsername(username);
        currentUser.addFriends(true, userRepository.findByUsername(friend.getUsername()));
        userRepository.save(currentUser);
    }

    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username, @Nonnull FriendJson invitation) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity inviteUser = userRepository.findByUsername(invitation.getUsername());

        FriendsEntity invite = currentUser.getInvites()
                .stream()
                .filter(fe -> fe.getUser().equals(inviteUser))
                .findFirst()
                .orElseThrow();

        invite.setPending(false);
        currentUser.addFriends(false, inviteUser);
        userRepository.save(currentUser);

        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
                        ? FriendState.INVITE_SENT
                        : FriendState.FRIEND))
                .toList();
    }

    public @Nonnull
    List<UserJson> declineInvitation(@Nonnull String username, @Nonnull FriendJson invitation) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendToDecline = userRepository.findByUsername(invitation.getUsername());
        currentUser.removeInvites(friendToDecline);
        currentUser.removeFriends(friendToDecline);
        userRepository.save(currentUser);
        return currentUser.getInvites()
                .stream()
                .filter(FriendsEntity::isPending)
                .map(fe -> UserJson.fromEntity(fe.getUser(), FriendState.INVITE_RECEIVED))
                .toList();
    }

    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username, @Nonnull String friendUsername) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendToRemove = userRepository.findByUsername(friendUsername);
        currentUser.removeFriends(friendToRemove);
        currentUser.removeInvites(friendToRemove);
        userRepository.save(currentUser);
        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()
                        ? FriendState.INVITE_SENT
                        : FriendState.FRIEND))
                .toList();
    }
}
