package niffler.service;

import jakarta.annotation.Nonnull;
import niffler.data.CurrencyValues;
import niffler.data.FriendsEntity;
import niffler.data.UserEntity;
import niffler.data.repository.UserRepository;
import niffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
        return userRepository.findByUsernameNot(username)
                .stream()
                .map(UserJson::fromEntity)
                .toList();
    }

    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        return userRepository.findByUsername(username)
                .getFriends()
                .stream()
                .filter(fe -> includePending || !fe.isPending())
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()))
                .toList();
    }

    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        return userRepository.findByUsername(username)
                .getInvites()
                .stream()
                .filter(FriendsEntity::isPending)
                .map(fe -> UserJson.fromEntity(fe.getUser()))
                .toList();
    }

    public @Nonnull
    List<UserJson> addFriend(String username, String friendUsername) {
        UserEntity currentUser = userRepository.findByUsername(username);
        currentUser.addFriends(true, userRepository.findByUsername(friendUsername));
        userRepository.save(currentUser);
        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()))
                .toList();
    }

    public @Nonnull
    List<UserJson> acceptInvitation(String username, String inviteUsername) {
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity inviteUser = userRepository.findByUsername(inviteUsername);

        List<FriendsEntity> acceptedInvitations = currentUser.getInvites()
                .stream()
                .filter(fe -> fe.getUser().equals(inviteUser))
                .peek(fe -> fe.setPending(false))
                .toList();

        currentUser.addFriends(false, inviteUser);
        userRepository.save(currentUser);

        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()))
                .toList();
    }

    public @Nonnull
    List<UserJson> removeFriend(String username, String friendUsername) {
        UserEntity currentUser = userRepository.findByUsername(username);
        currentUser.removeFriends(userRepository.findByUsername(friendUsername));
        userRepository.save(currentUser);
        return currentUser
                .getFriends()
                .stream()
                .map(fe -> UserJson.fromEntity(fe.getFriend(), fe.isPending()))
                .toList();
    }
}
