package niffler.test;

import generators.SpendGenerator;
import model.SpendDto;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spend.SpendClient;
import utils.AssertUtil;

import java.util.List;

public class SpendTest extends BaseTest {

    private final SpendClient client = new SpendClient();

    @Test
    @DisplayName("spends Корректный поиск созданного spend по пользователю")
    void addSpend() {
        SpendDto expected = SpendGenerator.defaultSpend();
        client.post(expected).assertThat().statusCode(HttpStatus.SC_CREATED);
        List<SpendDto> actualList = client
                .get(expected)
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", SpendDto.class);
        SpendDto actual = actualList.stream().filter(s -> s.getDescription().equals(expected.getDescription())).findFirst().orElse(null);
        AssertUtil.assertEquals(actual, expected, "spendDate", "id");
    }
}
