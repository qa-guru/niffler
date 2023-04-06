package niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import niffler.page.FriendsPage;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($(".header"));
    }

    private final SelenideElement friendsButton = self.$("a[href*='/friends']");

    public FriendsPage toFriendsPage() {
        friendsButton.click();
        return new FriendsPage();
    }
}
