package niffler.test;

import niffler.config.Config;
import niffler.jupiter.extension.JpaExtension;
import niffler.pages.HomePage;
import niffler.pages.LandingPage;
import niffler.pages.LoginPage;
import niffler.pages.ProfilePage;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JpaExtension.class)
public abstract class BaseTest {

    protected static final Config CFG = Config.getConfig();

    protected final LandingPage landingPage = new LandingPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final HomePage homePage = new HomePage();

    protected final ProfilePage profilePage = new ProfilePage();
}
