package guru.qa.niffler.api;

import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.SessionJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.StatisticJson;
import guru.qa.niffler.model.rest.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface GatewayApi {

  @GET("api/categories/all")
  Call<List<CategoryJson>> allCategories(@Header("Authorization") String bearerToken);

  @POST("api/categories/add")
  Call<CategoryJson> addCategory(@Header("Authorization") String bearerToken,
                                 @Body CategoryJson category);

  @GET("api/currencies/all")
  Call<List<CurrencyJson>> allCurrencies(@Header("Authorization") String bearerToken);

  @GET("api/friends/all")
  Call<List<UserJson>> allFriends(@Header("Authorization") String bearerToken,
                                  @Query("searchQuery") @Nonnull String searchQuery);

  @DELETE("api/friends/remove")
  Call<Void> removeFriend(@Header("Authorization") String bearerToken,
                          @Query("username") @Nullable String targetUsername);

  @POST("api/invitations/send")
  Call<UserJson> sendInvitation(@Header("Authorization") String bearerToken,
                                @Body FriendJson friend);

  @POST("api/invitations/accept")
  Call<UserJson> acceptInvitation(@Header("Authorization") String bearerToken,
                                  @Body FriendJson friend);

  @POST("api/invitations/decline")
  Call<UserJson> declineInvitation(@Header("Authorization") String bearerToken,
                                   @Body FriendJson friend);

  @GET("api/session/current")
  Call<SessionJson> currentSession(@Header("Authorization") String bearerToken);

  @GET("api/spends/all")
  Call<List<SpendJson>> allSpends(@Header("Authorization") String bearerToken,
                                  @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                  @Query("filterPeriod") @Nullable DataFilterValues filterPeriod);

  @POST("api/spends/add")
  Call<SpendJson> addSpend(@Header("Authorization") String bearerToken,
                           @Body SpendJson spend);

  @PATCH("api/spends/edit")
  Call<SpendJson> editSpend(@Header("Authorization") String bearerToken,
                            @Body SpendJson spend);

  @DELETE("api/spends/remove")
  Call<Void> removeSpends(@Header("Authorization") String bearerToken,
                          @Query("ids") @Nonnull List<String> ids);

  @GET("api/stat/total")
  Call<List<StatisticJson>> totalStat(@Header("Authorization") String bearerToken,
                                      @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                      @Query("filterPeriod") @Nullable DataFilterValues filterPeriod);

  @GET("api/users/current")
  Call<UserJson> currentUser(@Header("Authorization") String bearerToken);

  @GET("api/users/all")
  Call<List<UserJson>> allUsers(@Header("Authorization") String bearerToken,
                                @Query("searchQuery") @Nullable String searchQuery);

  @POST("api/users/update")
  Call<UserJson> updateUser(@Header("Authorization") String bearerToken,
                            @Body UserJson user);
}
