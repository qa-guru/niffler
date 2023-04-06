package niffler.test.rest;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import niffler.api.NifflerSpendClient;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.Spend;
import niffler.jupiter.annotation.User;
import niffler.model.rest.SpendJson;
import niffler.model.rest.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.qameta.allure.Allure.step;
import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-spend]: Траты")
@DisplayName("[REST][niffler-spend]: Траты")
public class NifflerSpendApiTest extends BaseRestTest {

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
        spend.setUsername(user.getUsername());
        final SpendJson created = nsc.createSpend(spend);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(created.getId().toString().matches(ID_REGEXP))
        );
    }
}
