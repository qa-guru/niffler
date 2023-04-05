package niffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.UserJson;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ProfileTest extends BaseTest {

    private static final String SUCCESS_MSG = "Profile updated!";

    @Test
    @DisplayName("WEB: Обновленное имя пользователя должно отображаться в профиле после обновления профиля")
    @AllureId("2")
    @ApiLogin(nifflerUser = @GenerateUser)
    void userNameShouldBeUpdatedAfterChangingInProfile(@User UserJson user) {
        ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
                .waitForPageLoaded()
                .checkUsername(user.getUsername())
                .setName("Pizzly")
                .submitProfile()
                .checkSuccessMessage(SUCCESS_MSG);

        Selenide.refresh();

        profilePage.checkUsername(user.getUsername())
                .checkName("Pizzly");
    }
}
