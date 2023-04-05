package niffler.test.rest;

import io.qameta.allure.AllureId;
import niffler.api.NifflerSpendClient;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.Spend;
import niffler.jupiter.annotation.User;
import niffler.model.rest.SpendJson;
import niffler.model.rest.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class NifflerSpendApiTest extends BaseRestTest {

    private final NifflerSpendClient nsc = new NifflerSpendClient();

    @AllureId("200004")
    @GenerateUser(categories = {
            @GenerateCategory("Рестораны"),
            @GenerateCategory("Бары"),
    })
    @ValueSource(strings = {
            "data/spend0.json",
            "data/spend1.json"
    })
    @ParameterizedTest(name = "REST: При создании нового spend возвращается ID из niffler-spend для запроса {0}")
    @Tag("REST")
    void apiShouldReturnIdOfCreatedSpend(@Spend SpendJson spend, @User(selector = METHOD) UserJson user) throws Exception {
        spend.setUsername(user.getUsername());
        SpendJson created = nsc.createSpend(spend);
        Assertions.assertNotNull(created.getId());
    }
}
