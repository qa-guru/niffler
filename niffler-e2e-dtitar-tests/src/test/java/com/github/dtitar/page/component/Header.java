package com.github.dtitar.page.component;

import com.codeborne.selenide.SelenideElement;
import com.github.dtitar.page.FriendsPage;
import io.qameta.allure.Step;

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
