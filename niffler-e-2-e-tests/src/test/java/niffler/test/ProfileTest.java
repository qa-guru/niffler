package niffler.test;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.api.NifflerAuthClient;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateUser;
import niffler.model.UserJson;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;


public class ProfileTest extends BaseTest{

    private final NifflerAuthClient registerClient = new NifflerAuthClient();


    @Test
    @AllureId("123")
    @ApiLogin(nifflerUser = @GenerateUser)
    void updateProfileTest(UserJson user) {
        ProfilePage profilePage = Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class);
        profilePage.checkPageLoaded()
                .setName("Пизлик")
                .submitProfile();

        Selenide.refresh();
        profilePage.checkName("Пизлик");
    }
}
