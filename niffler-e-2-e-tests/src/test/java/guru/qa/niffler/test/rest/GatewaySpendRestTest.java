package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.converter.SpendConverter;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.SimpleDateFormat;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[REST][niffler-gateway]: Spends")
@DisplayName("[REST][niffler-gateway]: Spends")
public class GatewaySpendRestTest extends BaseRestTest {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @AllureId("200008")
    @ValueSource(strings = {
            "rest/spend0.json",
            "rest/spend1.json"
    })
    @DisplayName("REST: При создании нового spend возвращается ID из niffler-spend")
    @ParameterizedTest(name = "Тестовые данные для запроса: {0}")
    @ApiLogin(
            user = @GenerateUser(categories = {
                    @GenerateCategory("Рестораны"),
                    @GenerateCategory("Бары"),
            })
    )
    @Tag("REST")
    void apiShouldReturnIdOfCreatedSpend(@ConvertWith(SpendConverter.class) SpendJson spend,
                                         @User UserJson user,
                                         @Token String bearerToken) throws Exception {
        final SpendJson created = gatewayApiClient.addSpend(
                bearerToken,
                spend.addUsername(user.username())
        );

        step("Check that response contains ID (GUID)", () ->
                assertTrue(created.id().toString().matches(ID_REGEXP))
        );
        step("Check that response contains username", () ->
                assertEquals(user.username(), created.username())
        );
    }
}
