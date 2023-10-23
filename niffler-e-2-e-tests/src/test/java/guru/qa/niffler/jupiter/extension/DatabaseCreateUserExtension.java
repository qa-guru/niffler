package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.ud.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public class DatabaseCreateUserExtension extends AbstractCreateUserExtension {

    private final UserRepository userRepository = UserRepository.getRepository();

    @Step("Create user for test (DB)")
    @Override
    @Nonnull
    protected UserJson createUser(@Nonnull String username,
                                  @Nonnull String password) throws Exception {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(password);
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.addAuthorities(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    return ae;
                }).toList());

        userRepository.createUserForTest(authUser);
        UserEntity userFromUserdata = userRepository.getTestUserFromUserdata(username);
        return new UserJson(
                userFromUserdata.getId(),
                username,
                userFromUserdata.getFirstname(),
                userFromUserdata.getSurname(),
                CurrencyValues.valueOf(userFromUserdata.getCurrency().name()),
                userFromUserdata.getPhoto() != null ? new String(userFromUserdata.getPhoto()) : null,
                null,
                new TestData(
                        password,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                )
        );
    }

    @Step("Create income invitations for test user (DB)")
    @Override
    protected void createIncomeInvitationsIfPresent(@Nonnull IncomeInvitations incomeInvitations,
                                                    @Nonnull UserJson createdUser) throws Exception {
        if (incomeInvitations.handleAnnotation() && incomeInvitations.count() > 0) {
            UserEntity targetUser = userRepository.getTestUserFromUserdata(createdUser.username());
            for (int i = 0; i < incomeInvitations.count(); i++) {
                UserJson incomeInvitation = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity incomeInvitationUser = userRepository.getTestUserFromUserdata(incomeInvitation.username());
                incomeInvitationUser.addFriends(true, targetUser);
                userRepository.updateUserForTest(incomeInvitationUser);
                createdUser.testData().invitationsJsons().add(incomeInvitation);
            }
        }
    }

    @Step("Create outcome invitations for test user (DB)")
    @Override
    protected void createOutcomeInvitationsIfPresent(@Nonnull OutcomeInvitations outcomeInvitations,
                                                     @Nonnull UserJson createdUser) throws Exception {
        if (outcomeInvitations.handleAnnotation() && outcomeInvitations.count() > 0) {
            UserEntity targetUser = userRepository.getTestUserFromUserdata(createdUser.username());
            for (int i = 0; i < outcomeInvitations.count(); i++) {
                UserJson outcomeInvitation = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity outcomeInvitationUser = userRepository.getTestUserFromUserdata(outcomeInvitation.username());
                targetUser.addFriends(true, outcomeInvitationUser);
                createdUser.testData().invitationsJsons().add(outcomeInvitation);
            }
            userRepository.updateUserForTest(targetUser);
        }
    }

    @Step("Create friends for test user (DB)")
    @Override
    protected void createFriendsIfPresent(@Nonnull Friends friends,
                                          @Nonnull UserJson createdUser) throws Exception {
        if (friends.handleAnnotation() && friends.count() > 0) {
            UserEntity targetUser = userRepository.getTestUserFromUserdata(createdUser.username());
            for (int i = 0; i < friends.count(); i++) {
                UserJson friend = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity friendUser = userRepository.getTestUserFromUserdata(friend.username());
                targetUser.addFriends(false, friendUser);
                friendUser.addFriends(false, targetUser);
                userRepository.updateUserForTest(friendUser);
                createdUser.testData().friendsJsons().add(friend);
            }
            userRepository.updateUserForTest(targetUser);
        }
    }
}
