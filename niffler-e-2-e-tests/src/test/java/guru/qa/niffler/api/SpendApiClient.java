package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.StatisticJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient {

  private final SpendApi spendApi;

  public SpendApiClient() {
    super(CFG.spendUrl());
    this.spendApi = retrofit.create(SpendApi.class);
  }

  @Step("Send REST POST('/internal/spends/add') request to niffler-spend")
  @Nullable
  public SpendJson createSpend(SpendJson spend) throws Exception {
    return spendApi.addSpend(spend)
        .execute()
        .body();
  }

  @Step("Send REST DELETE('/internal/spends/remove') request to niffler-spend")
  public void removeSpends(String username, String... ids) throws Exception {
    spendApi.removeSpends(username, Arrays.stream(ids).toList())
        .execute();
  }

  @Step("Send REST POST('/internal/categories/add') request to niffler-spend")
  @Nullable
  public CategoryJson createCategory(CategoryJson category) throws Exception {
    return spendApi.addCategory(category)
        .execute()
        .body();
  }

  @Step("Send REST PATCH('/internal/categories/update') request to niffler-spend")
  @Nonnull
  public CategoryJson updateCategory(CategoryJson category) throws Exception {
    return spendApi.updateCategory(category)
        .execute()
        .body();
  }

  @Step("Send REST GET('/internal/categories/all') request to niffler-spend")
  @Nonnull
  public List<CategoryJson> allCategories(String username) throws Exception {
    return spendApi.allCategories(username)
        .execute()
        .body();
  }

  @Step("Send REST GET('/internal/v2/spends/all') request to niffler-spend")
  @Nullable
  public RestPage<SpendJson> allSpendsPageable(String username,
                                               @Nullable CurrencyValues currency,
                                               @Nullable String from,
                                               @Nullable String to,
                                               @Nullable Integer page,
                                               @Nullable Integer size,
                                               @Nullable List<String> sort) throws Exception {
    return spendApi.allSpendsPageable(username, currency, from, to, page, size, sort)
        .execute()
        .body();
  }

  @Step("Send REST GET('/internal/stat/total') request to niffler-spend")
  @Nullable
  public List<StatisticJson> statistic(String username,
                                       @Nullable CurrencyValues userCurrency,
                                       @Nullable CurrencyValues filterCurrency,
                                       @Nullable String from,
                                       @Nullable String to) throws Exception {
    return spendApi.stat(username, userCurrency, filterCurrency, from, to)
        .execute()
        .body();
  }
}
