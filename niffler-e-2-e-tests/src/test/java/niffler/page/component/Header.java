package niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.page.FriendsPage;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($(".header"));
    }

    private final SelenideElement friendsButton = self.$("a[href*='/friends']");

    @Step("Open Friends page")
    public FriendsPage toFriendsPage() {
        friendsButton.click();
        return new FriendsPage();
    }
}
