package niffler.page;

import io.qameta.allure.Step;
import niffler.page.component.Footer;
import niffler.page.component.Header;
import niffler.page.component.SpendingTable;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl() + "main";

    protected final Header header = new Header();
    protected final Footer footer = new Footer();
    protected final SpendingTable spendingTable = new SpendingTable();

    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }

    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Step("Check that page is loaded")
    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler. The coin keeper."));
        footer.getSelf().should(visible).shouldHave(text("Study project for QA Automation Advanced. 2023"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of spendings"));
        return this;
    }
}
