package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitations;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.DateUtils;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static guru.qa.niffler.utils.DataUtils.generateRandomPassword;
import static guru.qa.niffler.utils.DataUtils.generateRandomUsername;

public class DatabaseCreateUserExtension extends AbstractCreateUserExtension {

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository = UserRepository.getInstance();
    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Step("Create user for test (DB)")
    @Override
    @Nonnull
    protected UserJson createUser(@Nonnull String username,
                                  @Nonnull String password) throws Exception {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
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

        UserEntity userdataUser = new UserEntity();
        userdataUser.setUsername(username);
        userdataUser.setCurrency(guru.qa.niffler.data.entity.CurrencyValues.RUB);

        userRepository.createInAuth(authUser);
        userdataUser = userRepository.createInUserdata(userdataUser);

        return new UserJson(
                userdataUser.getId(),
                username,
                userdataUser.getFirstname(),
                userdataUser.getSurname(),
                CurrencyValues.valueOf(userdataUser.getCurrency().name()),
                userdataUser.getPhoto() != null ? new String(userdataUser.getPhoto()) : null,
                userdataUser.getPhotoSmall() != null ? new String(userdataUser.getPhotoSmall()) : null,
                null,
                new TestData(
                        password,
                        new ArrayList<>(),
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
            UserEntity targetUser = userRepository.findByIdInUserdata(
                    createdUser.id()
            ).orElseThrow();
            for (int i = 0; i < incomeInvitations.count(); i++) {
                UserJson incomeInvitation = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity incomeInvitationUser = userRepository.findByIdInUserdata(
                        incomeInvitation.id()
                ).orElseThrow();
                incomeInvitationUser.addFriends(FriendshipStatus.PENDING, targetUser);
                userRepository.updateInUserdata(incomeInvitationUser);
                createdUser.testData().incomeInvitations().add(incomeInvitation);
            }
        }
    }

    @Step("Create outcome invitations for test user (DB)")
    @Override
    protected void createOutcomeInvitationsIfPresent(@Nonnull OutcomeInvitations outcomeInvitations,
                                                     @Nonnull UserJson createdUser) throws Exception {
        if (outcomeInvitations.handleAnnotation() && outcomeInvitations.count() > 0) {
            UserEntity targetUser = userRepository.findByIdInUserdata(
                    createdUser.id()
            ).orElseThrow();
            for (int i = 0; i < outcomeInvitations.count(); i++) {
                UserJson outcomeInvitation = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity outcomeInvitationUser = userRepository.findByIdInUserdata(
                        outcomeInvitation.id()
                ).orElseThrow();
                targetUser.addFriends(FriendshipStatus.PENDING, outcomeInvitationUser);
                createdUser.testData().outcomeInvitations().add(outcomeInvitation);
            }
            userRepository.updateInUserdata(targetUser);
        }
    }

    @Step("Create friends for test user (DB)")
    @Override
    protected void createFriendsIfPresent(@Nonnull Friends friends,
                                          @Nonnull UserJson createdUser) throws Exception {
        if (friends.handleAnnotation() && friends.count() > 0) {
            UserEntity targetUser = userRepository.findByIdInUserdata(
                    createdUser.id()
            ).orElseThrow();
            for (int i = 0; i < friends.count(); i++) {
                UserJson friend = createUser(generateRandomUsername(), generateRandomPassword());
                UserEntity friendUser = userRepository.findByIdInUserdata(
                        friend.id()
                ).orElseThrow();
                targetUser.addFriends(FriendshipStatus.ACCEPTED, friendUser);
                friendUser.addFriends(FriendshipStatus.ACCEPTED, targetUser);
                userRepository.updateInUserdata(friendUser);
                createdUser.testData().friends().add(friend);
            }
            userRepository.updateInUserdata(targetUser);
        }
    }

    @Override
    protected void createSpendsIfPresent(@Nullable GenerateSpend[] spends, @Nonnull UserJson createdUser) throws Exception {
        if (spends != null) {
            for (GenerateSpend spend : spends) {
                SpendEntity se = new SpendEntity();
                se.setUsername(createdUser.username());
                se.setCurrency(guru.qa.niffler.data.entity.CurrencyValues.valueOf(spend.currency().name()));
                se.setSpendDate(DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, spend.addDaysToSpendDate()));
                se.setAmount(spend.amount());
                se.setDescription(spend.spendName());
                se.setCategory(
                        spendRepository.findUserCategoryByName(
                                createdUser.username(),
                                spend.spendCategory()
                        ).orElseThrow()
                );
                se = spendRepository.createSpend(se);
                createdUser.testData().spends().add(
                        new SpendJson(
                                se.getId(),
                                se.getSpendDate(),
                                se.getAmount(),
                                CurrencyValues.valueOf(se.getCurrency().name()),
                                se.getCategory().getCategory(),
                                se.getDescription(),
                                createdUser.username()
                        )
                );
            }
        }
    }

    @Override
    protected void createCategoriesIfPresent(@Nullable GenerateCategory[] categories, @Nonnull UserJson createdUser) throws Exception {
        if (categories != null) {
            for (GenerateCategory category : categories) {
                CategoryEntity ce = new CategoryEntity();
                ce.setUsername(createdUser.username());
                ce.setCategory(category.value());
                ce = spendRepository.createCategory(ce);
                createdUser.testData().categories().add(
                        new CategoryJson(
                                ce.getId(),
                                ce.getCategory(),
                                ce.getUsername()
                        )
                );
            }
        }
    }
}
