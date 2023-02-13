package niffler.test;

import niffler.api.service.NifflerUserdataRestService;
import niffler.model.SpendJson;
import org.junit.jupiter.api.Test;

public class UserDataRestTest {

    private NifflerUserdataRestService nus = new NifflerUserdataRestService();

    @Test
    void updateUserApiTest() {
        SpendJson sj = new SpendJson();
        SpendJson.SpendDetailsJson sdj = new SpendJson.SpendDetailsJson();
        sj.setSpendDetails(sdj);

        nus.updateUser(null);
    }
}
