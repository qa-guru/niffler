package niffler.test;

import niffler.api.spend.dto.UserDto;
import niffler.api.spend.user.JsonToUser;
import niffler.api.spend.user.UserApi;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UpdateUserTest extends BaseTest {

    @ValueSource(strings = {
            "data/user0.json",
            "data/user1.json"
    })
    @ParameterizedTest
    void updateUser(@JsonToUser UserDto user) {
        UserApi.update(user)
                .verifyAfterUpdate(user);
    }

}
