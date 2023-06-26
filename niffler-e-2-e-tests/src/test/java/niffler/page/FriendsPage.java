package niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.model.rest.UserJson;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static niffler.condition.friends.FriendCondition.friends;

public class FriendsPage extends BasePage<FriendsPage> {

    public static final String URL = CFG.frontUrl() + "friends";

    private final SelenideElement tableContainer = $(".people-content");
    private final ElementsCollection friendRows = $$(".table tbody tr");

    @Override
    public FriendsPage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }

    @Step("Check that friends count is equal to {expectedCount}")
    public FriendsPage checkExistingFriendsCount(int expectedCount) {
        friendRows.shouldHave(size(expectedCount));
        return this;
    }

    @Step("Check that friends list contains data {0}")
    public FriendsPage checkExistingFriends(List<UserJson> expectedFriends) {
        friendRows.shouldHave(friends(expectedFriends));
        return this;
    }
}
