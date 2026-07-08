package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CreateCategoryMutation;
import guru.qa.CreateSpendMutation;
import guru.qa.DeleteSpendMutation;
import guru.qa.EditSpendMutation;
import guru.qa.GetSpendQuery;
import guru.qa.GetSpendsCsvQuery;
import guru.qa.GetSpendsQuery;
import guru.qa.UpdateCategoryMutation;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.type.CategoryInput;
import guru.qa.type.SpendInput;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[GraphQL][niffler-gateway]: Spends и Categories")
@DisplayName("[GraphQL][niffler-gateway]: Spends и Categories")
@ParametersAreNonnullByDefault
public class GraphQlSpendTest extends BaseGraphQlTest {

  @Test
  @AllureId("400009")
  @DisplayName("GraphQL: Mutation createSpend создаёт трату и возвращает её с id")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар")
  ))
  void createSpendMutationTest(@User UserJson user, @Token String bearerToken) throws Exception {
    ApolloCall<CreateSpendMutation.Data> apolloCall = apolloClient.mutation(
        new CreateSpendMutation(
            SpendInput.builder()
                .spendDate(new Date())
                .currency(guru.qa.type.CurrencyValues.RUB)
                .amount(500.0)
                .description("GraphQL коктейль")
                .category(CategoryInput.builder().name("Бар").archived(false).build())
                .build()
        )
    ).addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<CreateSpendMutation.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final CreateSpendMutation.Data data = response.dataOrThrow();
    final CreateSpendMutation.Spend spend = data.spend;

    step("Check that returned spend has ID (GUID)", () ->
        assertTrue(spend.id.matches(ID_REGEXP))
    );
    step("Check description", () ->
        assertEquals("GraphQL коктейль", spend.description)
    );
    step("Check amount", () ->
        assertEquals(500.0, spend.amount)
    );
    step("Check currency", () ->
        assertEquals(guru.qa.type.CurrencyValues.RUB, spend.currency)
    );
    step("Check category name", () ->
        assertEquals("Бар", spend.category.name)
    );
  }

  @Test
  @AllureId("400010")
  @DisplayName("GraphQL: Mutation editSpend обновляет существующую трату")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = @GenerateSpend(name = "Пиво", category = "Бар", amount = 100, currency = CurrencyValues.RUB)
  ))
  void editSpendMutationTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final String spendId = user.testData().spends().getFirst().id().toString();

    ApolloCall<EditSpendMutation.Data> apolloCall = apolloClient.mutation(
        new EditSpendMutation(
            SpendInput.builder()
                .id(spendId)
                .spendDate(new Date())
                .currency(guru.qa.type.CurrencyValues.RUB)
                .amount(250.0)
                .description("Крафтовое пиво")
                .category(CategoryInput.builder().name("Бар").archived(false).build())
                .build()
        )
    ).addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<EditSpendMutation.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final EditSpendMutation.Spend spend = response.dataOrThrow().spend;

    step("Check updated description", () ->
        assertEquals("Крафтовое пиво", spend.description)
    );
    step("Check updated amount", () ->
        assertEquals(250.0, spend.amount)
    );
    step("Check ID is preserved", () ->
        assertEquals(spendId, spend.id)
    );
  }

  @Test
  @AllureId("400011")
  @DisplayName("GraphQL: Mutation deleteSpend удаляет трату; список становится пустым")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = @GenerateSpend(name = "Коктейль", category = "Бар", amount = 300, currency = CurrencyValues.RUB)
  ))
  void deleteSpendMutationTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final String spendId = user.testData().spends().getFirst().id().toString();

    ApolloCall<DeleteSpendMutation.Data> deleteCall = apolloClient.mutation(
        new DeleteSpendMutation(List.of(spendId))
    ).addHttpHeader("Authorization", bearerToken);

    Rx2Apollo.single(deleteCall).blockingGet().dataOrThrow();

    ApolloCall<GetSpendsQuery.Data> listCall = apolloClient.query(
        GetSpendsQuery.builder().build()
    ).addHttpHeader("Authorization", bearerToken);

    final GetSpendsQuery.Data data = Rx2Apollo.single(listCall).blockingGet().dataOrThrow();

    step("Check that spend list is empty after deletion", () ->
        assertTrue(data.spends.edges.isEmpty())
    );
  }

  @Test
  @AllureId("400012")
  @DisplayName("GraphQL: Query getSpends возвращает список трат пользователя")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = {
          @GenerateSpend(name = "Коктейль", category = "Бар", amount = 300, currency = CurrencyValues.RUB),
          @GenerateSpend(name = "Кофе", category = "Бар", amount = 150, currency = CurrencyValues.RUB)
      }
  ))
  void getSpendsQueryTest(@Token String bearerToken) throws Exception {
    ApolloCall<GetSpendsQuery.Data> apolloCall = apolloClient.query(
        GetSpendsQuery.builder().build()
    ).addHttpHeader("Authorization", bearerToken);

    final GetSpendsQuery.Data data = Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow();

    step("Check that 2 spends are returned", () ->
        assertEquals(2, data.spends.edges.size())
    );
  }

  @Test
  @AllureId("400013")
  @DisplayName("GraphQL: Query getSpend возвращает трату по ID")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = @GenerateSpend(name = "Коктейль", category = "Бар", amount = 500, currency = CurrencyValues.RUB)
  ))
  void getSpendQueryTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final String spendId = user.testData().spends().getFirst().id().toString();

    ApolloCall<GetSpendQuery.Data> apolloCall = apolloClient.query(
        new GetSpendQuery(spendId)
    ).addHttpHeader("Authorization", bearerToken);

    final GetSpendQuery.Spend spend = Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow().spend;

    step("Check that returned spend ID matches", () ->
        assertEquals(spendId, spend.id)
    );
    step("Check description", () ->
        assertEquals("Коктейль", spend.description)
    );
    step("Check amount", () ->
        assertEquals(500.0, spend.amount)
    );
  }

  @Test
  @AllureId("400014")
  @DisplayName("GraphQL: Query getSpendsCsv возвращает непустой CSV")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар"),
      spends = @GenerateSpend(name = "Коктейль", category = "Бар", amount = 300, currency = CurrencyValues.RUB)
  ))
  void getSpendsCsvQueryTest(@Token String bearerToken) throws Exception {
    ApolloCall<GetSpendsCsvQuery.Data> apolloCall = apolloClient.query(
        new GetSpendsCsvQuery()
    ).addHttpHeader("Authorization", bearerToken);

    final GetSpendsCsvQuery.Data data = Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow();

    step("Check CSV is not null/empty", () -> {
      assertNotNull(data.spendsCsv);
      assertFalse(data.spendsCsv.isEmpty());
    });
  }

  @Test
  @AllureId("400015")
  @DisplayName("GraphQL: Mutation createCategory создаёт категорию и возвращает её с id")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser)
  void createCategoryMutationTest(@Token String bearerToken) throws Exception {
    ApolloCall<CreateCategoryMutation.Data> apolloCall = apolloClient.mutation(
        new CreateCategoryMutation(
            CategoryInput.builder()
                .name("GraphQL-категория")
                .archived(false)
                .build()
        )
    ).addHttpHeader("Authorization", bearerToken);

    final CreateCategoryMutation.Category category =
        Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow().category;

    step("Check category has ID (GUID)", () ->
        assertTrue(category.id.matches(ID_REGEXP))
    );
    step("Check category name", () ->
        assertEquals("GraphQL-категория", category.name)
    );
    step("Check category is not archived", () ->
        assertTrue(!category.archived)
    );
  }

  @Test
  @AllureId("400016")
  @DisplayName("GraphQL: Mutation updateCategory архивирует категорию")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Путешествия")
  ))
  void updateCategoryMutationTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final guru.qa.niffler.model.rest.CategoryJson existing = user.testData().categories().getFirst();

    ApolloCall<UpdateCategoryMutation.Data> apolloCall = apolloClient.mutation(
        new UpdateCategoryMutation(
            CategoryInput.builder()
                .id(existing.id().toString())
                .name(existing.name())
                .archived(true)
                .build()
        )
    ).addHttpHeader("Authorization", bearerToken);

    final UpdateCategoryMutation.Category category =
        Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow().category;

    step("Check category is now archived", () ->
        assertTrue(category.archived)
    );
    step("Check category name is preserved", () ->
        assertEquals("Путешествия", category.name)
    );
  }
}
