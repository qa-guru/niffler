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

import java.util.List;

public interface GatewayV2Api {

    @GET("api/v2/spends/all")
    Call<RestPage<SpendJson>> allSpendsPageable(@Header("Authorization") String bearerToken,
                                                @Query("filterCurrency") CurrencyValues filterCurrency,
                                                @Query("filterPeriod") DataFilterValues filterPeriod,
                                                @Query("page") Integer page,
                                                @Query("size") Integer size,
                                                @Query("sort") List<String> sort);


    @GET("api/v2/friends/all")
    Call<RestPage<UserJson>> allFriendsPageable(@Header("Authorization") String bearerToken,
                                                @Query("searchQuery") String searchQuery,
                                                @Query("page") Integer page,
                                                @Query("size") Integer size,
                                                @Query("sort") List<String> sort);
}
