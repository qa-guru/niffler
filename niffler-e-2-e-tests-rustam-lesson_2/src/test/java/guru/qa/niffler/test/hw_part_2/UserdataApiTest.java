package guru.qa.niffler.test.hw_part_2;

import static guru.qa.niffler.test.hw_part_2.UserService.updateUserdata;
import static org.assertj.core.api.Assertions.assertThat;


import guru.qa.niffler.jupiter.annotation.ClasspathUserdata;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.AllureId;
import java.io.IOException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserdataApiTest extends BaseWebTest {

    @AllureId("107")
    @ValueSource(strings = {
        "testdata/emily.json",
        "testdata/anna.json"
    })
    @ParameterizedTest
    void loginParametrizedAnnotatedUserdataTest(@ClasspathUserdata UserJson user) throws IOException {

        UserJson updatedInfo = updateUserdata(user);

        assertThat(updatedInfo.getId()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(updatedInfo.getUsername());
        assertThat(user.getFirstname()).isEqualTo(updatedInfo.getFirstname());
        assertThat(user.getSurname()).isEqualTo(updatedInfo.getSurname());
        assertThat(user.getCurrency()).isEqualTo(updatedInfo.getCurrency());
//        assertThat(user.getPhoto()).isEqualTo(updatedInfo.getPhoto());
    }


}
