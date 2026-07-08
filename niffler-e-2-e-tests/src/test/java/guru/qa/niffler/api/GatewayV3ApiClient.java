package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.page.PagedModelJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;
import okhttp3.ResponseBody;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class GatewayV3ApiClient extends RestClient {

  private final GatewayV3Api gatewayV3Api;

  public GatewayV3ApiClient() {
    super(CFG.gatewayUrl());
    this.gatewayV3Api = retrofit.create(GatewayV3Api.class);
  }

  @Step("Send REST GET('/api/v3/spends/all') request to niffler-gateway")
  @Nullable
  public PagedModelJson<SpendJson> allSpendsPageable(String bearerToken,
                                                     @Nullable CurrencyValues currency,
                                                     @Nullable DataFilterValues filterPeriod,
                                                     @Nullable Integer page,
                                                     @Nullable Integer size,
                                                     @Nullable List<String> sort) throws Exception {
    return gatewayV3Api.allSpendsPageable(bearerToken, currency, filterPeriod, page, size, sort, null, null)
        .execute()
        .body();
  }

  @Step("Send REST GET('/api/v3/spends/all') with search request to niffler-gateway")
  @Nullable
  public PagedModelJson<SpendJson> allSpendsPageableWithSearch(String bearerToken,
                                                               @Nullable CurrencyValues currency,
                                                               @Nullable DataFilterValues filterPeriod,
                                                               @Nullable Integer page,
                                                               @Nullable Integer size,
                                                               @Nullable List<String> sort,
                                                               @Nullable String searchQuery,
                                                               @Nullable String category) throws Exception {
    return gatewayV3Api.allSpendsPageable(bearerToken, currency, filterPeriod, page, size, sort, searchQuery, category)
        .execute()
        .body();
  }

  @Step("Send REST GET('/api/v3/spends/export/csv') request to niffler-gateway")
  @Nullable
  public ResponseBody exportSpendsCsv(String bearerToken) throws Exception {
    return gatewayV3Api.exportSpendsCsv(bearerToken)
        .execute()
        .body();
  }

  @Step("Send REST GET('/api/v3/users/all') request to niffler-gateway")
  @Nullable
  public PagedModelJson<UserJson> allUsersPageable(String bearerToken,
                                                   @Nullable String searchQuery,
                                                   @Nullable Integer page,
                                                   @Nullable Integer size,
                                                   @Nullable List<String> sort) throws Exception {
    return gatewayV3Api.allUsersPageable(bearerToken, searchQuery, page, size, sort)
        .execute()
        .body();
  }

  @Step("Send REST GET('/api/v3/friends/all') request to niffler-gateway")
  @Nullable
  public PagedModelJson<UserJson> allFriendsPageable(String bearerToken,
                                                     @Nullable String searchQuery,
                                                     @Nullable Integer page,
                                                     @Nullable Integer size,
                                                     @Nullable List<String> sort) throws Exception {
    return gatewayV3Api.allFriendsPageable(bearerToken, searchQuery, page, size, sort)
        .execute()
        .body();
  }
}
