package niffler.tests;

import niffler.pages.HomePage;
import niffler.pages.LandingPage;
import niffler.pages.LoginPage;

public abstract class BaseTest {

    protected final LandingPage landingPage = new LandingPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final HomePage homePage = new HomePage();

}
