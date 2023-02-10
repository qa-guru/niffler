package niffler.pages;

import lombok.Getter;
import niffler.pages.components.SpendingTableComponent;

public class HomePage extends BasePage {

    @Getter
    public final SpendingTableComponent spendingTable = new SpendingTableComponent();

}
