package niffler.test;

import com.codeborne.selenide.Selenide;
import niffler.jupiter.annotation.ApiLogin;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;


public class ProfileTest extends BaseTest{

    @Test
    @ApiLogin(username = "admin", password = "12345")
    void updateProfileTest() {
        ProfilePage profilePage = Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class);
        profilePage.checkPageLoaded()
                .setName("Пизлик")
                .submitProfile();

        Selenide.refresh();
        profilePage.checkName("Пизлик");
    }
}
