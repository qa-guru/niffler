package guru.qa.niffler.api;

import guru.qa.niffler.model.page.PagedModelJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import java.util.List;

public interface GatewayV3Api {

  @GET("api/v3/spends/all")
  Call<PagedModelJson<SpendJson>> allSpendsPageable(@Header("Authorization") String bearerToken,
                                                    @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                                    @Query("filterPeriod") @Nullable DataFilterValues filterPeriod,
                                                    @Query("page") @Nullable Integer page,
                                                    @Query("size") @Nullable Integer size,
                                                    @Query("sort") @Nullable List<String> sort);

  @GET("api/v3/users/all")
  Call<PagedModelJson<UserJson>> allUsersPageable(@Header("Authorization") String bearerToken,
                                                  @Query("searchQuery") @Nullable String searchQuery,
                                                  @Query("page") @Nullable Integer page,
                                                  @Query("size") @Nullable Integer size,
                                                  @Query("sort") @Nullable List<String> sort);

  @GET("api/v3/friends/all")
  Call<PagedModelJson<UserJson>> allFriendsPageable(@Header("Authorization") String bearerToken,
                                                    @Query("searchQuery") @Nullable String searchQuery,
                                                    @Query("page") @Nullable Integer page,
                                                    @Query("size") @Nullable Integer size,
                                                    @Query("sort") @Nullable List<String> sort);
}
