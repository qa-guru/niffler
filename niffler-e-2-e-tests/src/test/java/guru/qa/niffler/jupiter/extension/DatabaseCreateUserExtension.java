package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.ud.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.Arrays;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public class DatabaseCreateUserExtension extends AbstractCreateUserExtension {

    private final UserRepository userRepository = UserRepository.getRepository();

    @Step("Create user for test (DB)")
    @Override
    protected UserJson createUser(String username, String password) throws Exception {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(password);
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(authUser);
                    return ae;
                }).toList()));

        userRepository.createUserForTest(authUser);
        UserEntity userFromUserdata = userRepository.getTestUserFromUserdata(username);
        UserJson result = new UserJson();
        result.setId(userFromUserdata.getId());
        result.setUsername(username);
        result.setPassword(password);
        result.setCurrency(CurrencyValues.valueOf(userFromUserdata.getCurrency().name()));
        return result;
    }

    @Step("Create income invitations for test user (DB)")
    @Override
    protected void createIncomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        IncomeInvitations invitations = generateUser.incomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            UserEntity targetUser = userRepository.getTestUserFromUserdata(createdUser.getUsername());
            for (int i = 0; i < invitations.count(); i++) {
                UserJson incomeInvitation = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity incomeInvitationUser = userRepository.getTestUserFromUserdata(incomeInvitation.getUsername());
                incomeInvitationUser.addFriends(true, targetUser);
                userRepository.updateUserForTest(incomeInvitationUser);
                createdUser.getInvitationsJsons().add(incomeInvitation);
            }
        }
    }

    @Step("Create outcome invitations for test user (DB)")
    @Override
    protected void createOutcomeInvitationsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        OutcomeInvitations invitations = generateUser.outcomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            UserEntity targetUser = userRepository.getTestUserFromUserdata(createdUser.getUsername());
            for (int i = 0; i < invitations.count(); i++) {
                UserJson outcomeInvitation = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity outcomeInvitationUser = userRepository.getTestUserFromUserdata(outcomeInvitation.getUsername());
                targetUser.addFriends(true, outcomeInvitationUser);
                createdUser.getInvitationsJsons().add(outcomeInvitation);
            }
            userRepository.updateUserForTest(targetUser);
        }
    }

    @Step("Create friends for test user (DB)")
    @Override
    protected void createFriendsIfPresent(GenerateUser generateUser, UserJson createdUser) throws Exception {
        Friends friends = generateUser.friends();
        if (friends.handleAnnotation() && friends.count() > 0) {
            UserEntity targetUser = userRepository.getTestUserFromUserdata(createdUser.getUsername());
            for (int i = 0; i < friends.count(); i++) {
                UserJson friend = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity friendUser = userRepository.getTestUserFromUserdata(friend.getUsername());
                targetUser.addFriends(false, friendUser);
                friendUser.addFriends(false, targetUser);
                userRepository.updateUserForTest(friendUser);
                createdUser.getFriendsJsons().add(friend);
            }
            userRepository.updateUserForTest(targetUser);
        }
    }
}
