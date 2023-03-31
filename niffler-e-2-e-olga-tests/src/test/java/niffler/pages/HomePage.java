package niffler.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;
import niffler.pages.components.SpendingTableComponent;

import static com.codeborne.selenide.Selenide.$;

public class HomePage extends BasePage {

    @Getter
    public final SpendingTableComponent spendingTable = new SpendingTableComponent();

    public SelenideElement usernameLink = $(".header__username");

    @Step("Check username on home page")
    public HomePage checkUsername(String username) {
        usernameLink.shouldHave(Condition.text(username));
        return this;
    }

}
