package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.CategoryRustamCondition.category;
import static guru.qa.niffler.condition.SpendRustamCondition.spends;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.Spend7Json;
import io.qameta.allure.Step;

public class CategoryRustamComponent extends BaseComponentRustam<CategoryRustamComponent> {

    public CategoryRustamComponent() {
        super($(".main-content__section-history"));
    }

    @Step("Check that table contains data {0}")
    public CategoryRustamComponent checkTableContainsCategory(CategoryJson... expectedCategory) {
        self.$$("tbody tr").should(category(expectedCategory));
        return this;
    }

    @Override
    public CategoryRustamComponent checkThatComponentDisplayed() {
        return null;
    }
}
