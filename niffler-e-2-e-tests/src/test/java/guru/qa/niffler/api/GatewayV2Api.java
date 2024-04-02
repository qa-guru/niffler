package guru.qa.niffler.api;

import guru.qa.niffler.model.page.RestPage;
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

public interface GatewayV2Api {

    @GET("api/v2/spends/all")
    Call<RestPage<SpendJson>> allSpendsPageable(@Header("Authorization") String bearerToken,
                                                @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                                @Query("filterPeriod") @Nullable DataFilterValues filterPeriod,
                                                @Query("page") @Nullable Integer page,
                                                @Query("size") @Nullable Integer size,
                                                @Query("sort") @Nullable List<String> sort);

    @GET("api/v2/users/all")
    Call<RestPage<UserJson>> allUsersPageable(@Header("Authorization") String bearerToken,
                                              @Query("searchQuery") @Nullable String searchQuery,
                                              @Query("page") @Nullable Integer page,
                                              @Query("size") @Nullable Integer size,
                                              @Query("sort") @Nullable List<String> sort);

    @GET("api/v2/friends/all")
    Call<RestPage<UserJson>> allFriendsPageable(@Header("Authorization") String bearerToken,
                                                @Query("searchQuery") @Nullable String searchQuery,
                                                @Query("page") @Nullable Integer page,
                                                @Query("size") @Nullable Integer size,
                                                @Query("sort") @Nullable List<String> sort);

    @GET("api/v2/invitations/income")
    Call<RestPage<UserJson>> incomeInvitationsPageable(@Header("Authorization") String bearerToken,
                                                       @Query("searchQuery") @Nullable String searchQuery,
                                                       @Query("page") @Nullable Integer page,
                                                       @Query("size") @Nullable Integer size,
                                                       @Query("sort") @Nullable List<String> sort);

    @GET("api/v2/invitations/outcome")
    Call<RestPage<UserJson>> outcomeInvitationsPageable(@Header("Authorization") String bearerToken,
                                                        @Query("searchQuery") @Nullable String searchQuery,
                                                        @Query("page") @Nullable Integer page,
                                                        @Query("size") @Nullable Integer size,
                                                        @Query("sort") @Nullable List<String> sort);
}
