package niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    public SelenideElement currencySelector = $("span[id^='react-select']").parent();
    }

