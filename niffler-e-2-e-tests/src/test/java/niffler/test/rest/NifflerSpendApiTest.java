package niffler.test.rest;

import io.qameta.allure.AllureId;
import niffler.api.NifflerSpendClient;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.Spend;
import niffler.jupiter.annotation.User;
import niffler.model.SpendJson;
import niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static niffler.jupiter.extension.CreateUserExtension.Selector.METHOD;

public class NifflerSpendApiTest {

    private final NifflerSpendClient nsc = new NifflerSpendClient();

    @AllureId("100")
    @GenerateUser(categories = {
            @GenerateCategory("Рестораны"),
            @GenerateCategory("Бары"),
    })
    @ValueSource(strings = {
            "data/spend0.json",
            "data/spend1.json"
    })
    @ParameterizedTest
    void apiShouldReturnIdOfCreatedSpend(@Spend SpendJson spend, @User(selector = METHOD) UserJson user) throws Exception {
        spend.setUsername(user.getUserName());
        SpendJson created = nsc.createSpend(spend);
        Assertions.assertNotNull(created.getId());
    }
}
