package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.GatewayV2ApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("[REST][niffler-gateway]: Пагинация")
@DisplayName("[REST][niffler-gateway]: Пагинация")
public class GatewayV2RestTest extends BaseRestTest {

    private static final GatewayV2ApiClient gatewayV2client = new GatewayV2ApiClient();

    @CsvSource({
            "description, ASC, Коктейль, Кофе",
            "description, DESC, Орешки, Кофе"
    })
    @ParameterizedTest
    @AllureId("200001")
    @DisplayName("REST: Список spends получен в виде Page при передаче параметров page, size c учетом сортировки по полю description")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            categories = @GenerateCategory("Бар"),
            spends = {
                    @GenerateSpend(spendName = "Коктейль", spendCategory = "Бар", amount = 650),
                    @GenerateSpend(spendName = "Кофе", spendCategory = "Бар", amount = 200),
                    @GenerateSpend(spendName = "Орешки", spendCategory = "Бар", amount = 300),
            }
    ))
    void pageableSpendsWithSortTest(String sortField,
                                    Sort.Direction direction,
                                    String firstExpected,
                                    String secondExpected,
                                    @Token String bearerToken) throws Exception {
        RestPage<SpendJson> firstPage = gatewayV2client.allSpendsPageable(
                bearerToken,
                null,
                null,
                0,
                2,
                List.of(sortField + "," + direction.name())
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(3L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(2, firstPage.getTotalPages())
        );
        final List<SpendJson> firstPageContent = firstPage.getContent();

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals(firstExpected, firstPageContent.get(0).description())
        );
        step("Check second element of first page", () ->
                assertEquals(secondExpected, firstPageContent.get(1).description())
        );
    }

    @Test
    @AllureId("200002")
    @DisplayName("REST: Список spends получен в виде Page при передаче параметров page, size с фильтрацией по Currency")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            categories = @GenerateCategory("Бар"),
            spends = {
                    @GenerateSpend(spendName = "Коктейль", spendCategory = "Бар", amount = 650, currency = CurrencyValues.RUB),
                    @GenerateSpend(spendName = "Кофе", spendCategory = "Бар", amount = 200, currency = CurrencyValues.RUB),
                    @GenerateSpend(spendName = "Орешки", spendCategory = "Бар", amount = 3, currency = CurrencyValues.USD),
            }
    ))
    void pageableSpendsTestWithCurrency(@Token String bearerToken) throws Exception {
        RestPage<SpendJson> firstPage = gatewayV2client.allSpendsPageable(
                bearerToken,
                CurrencyValues.RUB,
                null,
                0,
                2,
                null
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(2L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(1, firstPage.getTotalPages())
        );
        final List<SpendJson> firstPageContent = firstPage.getContent();

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals("Коктейль", firstPageContent.get(0).description())
        );
        step("Check second element of first page", () ->
                assertEquals("Кофе", firstPageContent.get(1).description())
        );
    }

    @Test
    @AllureId("200003")
    @DisplayName("REST: Список spends получен в виде Page при передаче параметров page, size c учетом сортировки по полю username ASC")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            friends = @Friends(
                    count = 3
            )
    ))
    void pageableFriendsWithSortAscTest(@User UserJson user,
                                        @Token String bearerToken) throws Exception {
        RestPage<UserJson> firstPage = gatewayV2client.allFriendsPageable(
                bearerToken,
                null,
                0,
                2,
                List.of("username," + Sort.Direction.ASC)
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(3L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(2, firstPage.getTotalPages())
        );
        final List<UserJson> firstPageContent = firstPage.getContent();

        List<UserJson> userFriends = user.testData().friends();
        Collections.sort(userFriends, Comparator.comparing(UserJson::username));

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals(userFriends.get(0).username(), firstPageContent.get(0).username())
        );
        step("Check second element of first page", () ->
                assertEquals(userFriends.get(1).username(), firstPageContent.get(1).username())
        );
    }

    @Test
    @AllureId("200004")
    @DisplayName("REST: Список spends получен в виде Page при передаче параметров page, size c учетом сортировки по полю username DESC")
    @Tag("REST")
    @ApiLogin(user = @GenerateUser(
            friends = @Friends(
                    count = 3
            )
    ))
    void pageableFriendsWithSortDescTest(@User UserJson user,
                                         @Token String bearerToken) throws Exception {
        RestPage<UserJson> firstPage = gatewayV2client.allFriendsPageable(
                bearerToken,
                null,
                0,
                2,
                List.of("username," + Sort.Direction.DESC)
        );
        step("Check that response not null", () ->
                assertNotNull(firstPage)
        );
        step("Check total elements count", () ->
                assertEquals(3L, firstPage.getTotalElements())
        );
        step("Check total pages", () ->
                assertEquals(2, firstPage.getTotalPages())
        );
        final List<UserJson> firstPageContent = firstPage.getContent();

        List<UserJson> userFriends = user.testData().friends();
        Collections.sort(userFriends, Comparator.comparing(UserJson::username));
        Collections.reverse(userFriends);

        step("Check elements size", () ->
                assertEquals(2, firstPageContent.size())
        );
        step("Check first element of first page", () ->
                assertEquals(userFriends.get(0).username(), firstPageContent.get(0).username())
        );
        step("Check second element of first page", () ->
                assertEquals(userFriends.get(1).username(), firstPageContent.get(1).username())
        );
    }
}
