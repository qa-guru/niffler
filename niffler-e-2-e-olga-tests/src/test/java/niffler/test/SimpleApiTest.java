package niffler.test;

import niffler.api.NifflerSpendClient;
import niffler.api.NifflerUserdataClient;
import niffler.jupiter.Spend;
import niffler.jupiter.UserData;
import niffler.model.SpendJson;
import niffler.model.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SimpleApiTest {

    private NifflerSpendClient nsc = new NifflerSpendClient();

    private NifflerUserdataClient nifflerUserdataClient = new NifflerUserdataClient();

    @ValueSource(strings = {"data/spend0.json", "data/spend1.json", "data/user0.json", "data/user1.json"})

    @ParameterizedTest
    void addSpend(@Spend SpendJson spend) throws Exception {
        SpendJson created = nsc.createSpend(spend);
        Assertions.assertNotNull(created.getId());
    }

    @ParameterizedTest
    void updateUserInfo(@UserData UserJson user) throws Exception {
        UserJson updatedInfo = nifflerUserdataClient.updateUserInfo(user);
        Assertions.assertNotNull(updatedInfo.getId());
    }
}
