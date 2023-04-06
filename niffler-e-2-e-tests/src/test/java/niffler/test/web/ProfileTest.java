package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.rest.CurrencyValues;
import niffler.model.rest.UserJson;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static niffler.utils.DataUtils.generateNewCategory;
import static niffler.utils.DataUtils.generateRandomName;
import static niffler.utils.DataUtils.generateRandomSurname;
import static niffler.utils.Error.CAN_NOT_ADD_CATEGORY;

@Epic("[WEB][niffler-frontend]: Профиль")
@DisplayName("[WEB][niffler-frontend]: Профиль")
public class ProfileTest extends BaseWebTest {

    private static final String SUCCESS_MSG = "Profile updated!";

    @Test
    @AllureId("500004")
    @DisplayName("WEB: Пользователь может отредактировать все поля в профиле")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser)
    void shouldUpdateProfileWithAllFieldsSet(@User UserJson user) {
        String newName = generateRandomName();
        String newSurname = generateRandomSurname();

        ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded()
                .setName(newName)
                .setSurname(newSurname)
                .setCurrency(CurrencyValues.EUR)
                .submitProfile()
                .checkToasterMessage(SUCCESS_MSG);

        Selenide.refresh();

        profilePage.checkName(newName);
        profilePage.checkSurname(newSurname);
        profilePage.checkCurrency(CurrencyValues.EUR);
    }

    @Test
    @AllureId("500005")
    @DisplayName("WEB: Пользователь может отредактировать профиль с заполнением только обязательных полей")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser)
    void shouldUpdateProfileWithOnlyRequiredFields(@User UserJson user) {
        ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded()
                .setCurrency(CurrencyValues.KZT)
                .submitProfile()
                .checkToasterMessage(SUCCESS_MSG);

        Selenide.refresh();

        profilePage.checkName("");
        profilePage.checkSurname("");
        profilePage.checkCurrency(CurrencyValues.KZT);
    }

    @Test
    @AllureId("500006")
    @DisplayName("WEB: Пользователь имеет возможность добавить категорию трат")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser)
    void shouldAddNewCategory(@User UserJson user) {
        String newCategory = generateNewCategory();
        ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded()
                .addCategory(newCategory);
        Selenide.refresh();
        profilePage.checkCategoryExists(newCategory);
    }

    @Test
    @AllureId("500007")
    @DisplayName("WEB: Пользователь не имеет возможности добавить более 8 трат")
    @Tag("WEB")
    @ApiLogin(nifflerUser = @GenerateUser(
            categories = {
                    @GenerateCategory("Food"),
                    @GenerateCategory("Bars"),
                    @GenerateCategory("Clothes"),
                    @GenerateCategory("Friends"),
                    @GenerateCategory("Music"),
                    @GenerateCategory("Sports"),
                    @GenerateCategory("Walks"),
                    @GenerateCategory("Books")
            }
    ))
    void shouldForbidAddingMoreThat8Categories(@User UserJson user) {
        String newCategory = generateNewCategory();
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded()
                .addCategory(newCategory)
                .checkToasterMessage(CAN_NOT_ADD_CATEGORY.content);
    }
}
