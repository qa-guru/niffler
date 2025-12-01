package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.List;

import static guru.qa.niffler.model.rest.CurrencyValues.RUB;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("[GraphQL][niffler-gateway]: Stat")
@DisplayName("[GraphQL][niffler-gateway]: Stat")
public class GraphQlStatTest extends BaseGraphQlTest {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

  @Test
  @DisplayName("REST: Статистика содержит корректные данные при рассчете за все время и без фильтрации по валюте")
  @AllureId("400008")
  @Tag("GraphQL")
  @ApiLogin(
      user = @GenerateUser(
          categories = {@GenerateCategory(name = "Бар"), @GenerateCategory(name = "Азс")},
          spends = {
              @GenerateSpend(name = "Коктейль", category = "Бар", amount = 650, currency = RUB, addDaysToSpendDate = -2),
              @GenerateSpend(name = "Кофе", category = "Бар", amount = 200, currency = RUB, addDaysToSpendDate = -1),
              @GenerateSpend(name = "Бензин", category = "Азс", amount = 300, currency = RUB),
          }
      )
  )
  void statForOpenPeriodWithoutFilteringTest(@User UserJson user,
                                             @Token String bearerToken) throws Exception {
    ApolloCall<StatQuery.Data> apolloCall = apolloClient.query(StatQuery.builder().build())
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final StatQuery.Data responseData = response.dataOrThrow();
    final StatQuery.Stat stat = responseData.stat;

    step("Check that response contains statistic in default user currency (RUB)", () ->
        assertEquals(guru.qa.type.CurrencyValues.RUB, stat.currency)
    );
    step("Check that response contains correct total summ", () ->
        assertEquals(1150.00, stat.total)
    );

    final List<StatQuery.StatByCategory> statByCategories = stat.statByCategories;

    step("Check that response contains all categories stat", () ->
        assertEquals(2, statByCategories.size())
    );
  }
}
