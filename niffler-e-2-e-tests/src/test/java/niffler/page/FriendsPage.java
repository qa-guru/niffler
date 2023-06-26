package niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.model.rest.UserJson;
import niffler.page.component.PeopleTable;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static niffler.condition.users.UsersCondition.users;

public class FriendsPage extends BasePage<FriendsPage> {

    public static final String URL = CFG.frontUrl() + "friends";

    private final SelenideElement tableContainer = $(".people-content");
    private final PeopleTable table = new PeopleTable($(".table"));

    @Override
    public FriendsPage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }

    @Step("Check that friends count is equal to {expectedCount}")
    public FriendsPage checkExistingFriendsCount(int expectedCount) {
        table.getAllRows().shouldHave(size(expectedCount));
        return this;
    }

    @Step("Check that friends list contains data {0}")
    public FriendsPage checkExistingFriends(List<UserJson> expectedFriends) {
        table.getAllRows().filter(text("You are friends")).shouldHave(users(expectedFriends));
        return this;
    }

    @Step("Delete user from friends: {username}")
    public FriendsPage removeFriend(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.$(".button-icon_type_close")
                .click();
        return this;
    }

    @Step("Accept invitation from user: {username}")
    public FriendsPage acceptFriendInvitationFromUser(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.$(".button-icon_type_submit")
                .click();
        return this;
    }
}
