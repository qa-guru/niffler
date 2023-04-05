package niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage>{
    public static final String URL = CFG.frontUrl() + "friends";
    private final SelenideElement tableContainer = $(".people-content");

    @Override
    public FriendsPage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }
}
