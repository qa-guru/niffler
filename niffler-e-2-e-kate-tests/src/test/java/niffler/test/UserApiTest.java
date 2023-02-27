package niffler.test;

import niffler.model.UserJson;
import niffler.api.UsersService;
import niffler.jupiter.UserFromJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserApiTest {
    @ValueSource(strings = {
            "data/users/user0.json",
            "data//users/user1.json"
    })
    @ParameterizedTest
    void updateUserInfo(@UserFromJson UserJson user) {
        UserJson updatedInfo = UsersService.updateUser(user);

        Assertions.assertNotNull(updatedInfo.getId());
        Assertions.assertEquals(user.getUsername(), updatedInfo.getUsername());
        Assertions.assertEquals(user.getFirstname(), updatedInfo.getFirstname());
        Assertions.assertEquals(user.getSurname(), updatedInfo.getSurname());
        Assertions.assertEquals(user.getCurrency(), updatedInfo.getCurrency());
        Assertions.assertEquals(user.getPhoto(), updatedInfo.getPhoto());
    }
}
