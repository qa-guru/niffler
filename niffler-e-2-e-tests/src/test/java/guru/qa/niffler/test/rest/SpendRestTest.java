package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.NifflerSpendClient;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static guru.qa.niffler.jupiter.annotation.User.Selector.METHOD;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-spend]: Траты")
@DisplayName("[REST][niffler-spend]: Траты")
public class SpendRestTest extends BaseRestTest {

    private final NifflerSpendClient nsc = new NifflerSpendClient();

    @AllureId("200004")
    @GenerateUser(categories = {
            @GenerateCategory("Рестораны"),
            @GenerateCategory("Бары"),
    })
    @ValueSource(strings = {
            "rest/spend0.json",
            "rest/spend1.json"
    })
    @DisplayName("REST: При создании нового spend возвращается ID из niffler-spend")
    @ParameterizedTest(name = "Тестовые данные для запроса: {0}")
    @Tag("REST")
    void apiShouldReturnIdOfCreatedSpend(@Spend SpendJson spend, @User(selector = METHOD) UserJson user) throws Exception {
        final SpendJson created = nsc.createSpend(spend.addUsername(user.username()));

        step("Check that response contains ID (GUID)", () ->
                assertTrue(created.id().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), created.username())
        );
    }
}
