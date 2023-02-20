package niffler.page;

import io.qameta.allure.Step;
import niffler.page.component.Footer;
import niffler.page.component.Header;
import niffler.page.component.SpendingTable;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl() + "main";

    protected final Header header = new Header();
    protected final Footer footer = new Footer();
    protected final SpendingTable spendingTable = new SpendingTable();

    @Step("Redirect to login page")
    public LoginPage doLogin() {
        $("a[href*='redirect']").click();
        return new LoginPage();
    }

    @Step("Redirect to register page")
    public RegisterPage doRegister() {
        $("a[href*='register']").click();
        return new RegisterPage();
    }

    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }

    public SpendingTable getSpendingTable() {
        return spendingTable;
    }

    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler. The coin keeper."));
        footer.getSelf().should(visible).shouldHave(text("Study project for QA Automation Advanced. 2023"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of spendings"));
        return this;
    }
}
