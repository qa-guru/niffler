package niffler.test;

import niffler.page.MainPage;
import niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

public class WebTest {

    @Test
    void webtest() {
        ProfilePage pp = new ProfilePage()
                .waitForPageLoaded()
                .fillProfile();
    }


    @Test
    void webtest0() {
        MainPage mp = new MainPage()
                .waitForPageLoaded()
                .fillNewSpending();
    }
}
