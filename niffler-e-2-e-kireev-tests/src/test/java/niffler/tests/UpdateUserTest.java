package niffler.tests;

import niffler.api.user.UserApi;
import niffler.api.user.dto.UserDto;
import niffler.jupiter.user.JsonToUser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UpdateUserTest extends BaseTest {

    @ValueSource(strings = {
            "testData/user/test-user1.json",
            "testData/user/test-user2.json"
    })
    @ParameterizedTest
    void updateUser(@JsonToUser UserDto user) {
        UserApi.update(user)
                .verifyAfterUpdate(user);
    }

}
