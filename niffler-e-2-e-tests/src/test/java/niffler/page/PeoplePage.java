package niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import niffler.page.component.PeopleTable;

import static com.codeborne.selenide.Selenide.$;

public class PeoplePage extends BasePage<PeoplePage> {

    public static final String URL = CFG.frontUrl() + "people";

    private final SelenideElement tableContainer = $(".people-content");
    private final PeopleTable table = new PeopleTable($(".table"));

    @Override
    public PeoplePage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }

    public PeoplePage sendFriendInvitationToUser(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.$(".button-icon_type_add")
                .click();
        return this;
    }

    public PeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.shouldHave(Condition.text("Pending invitation"));
        return this;
    }
}
