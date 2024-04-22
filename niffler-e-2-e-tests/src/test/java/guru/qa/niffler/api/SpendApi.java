package guru.qa.niffler.api;

import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.StatisticJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import java.util.List;

public interface SpendApi {

    @POST("internal/spends/add")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @POST("internal/categories/add")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> allCategories(@Query("username") String username);

    @GET("internal/stat/total")
    Call<List<StatisticJson>> stat(@Query("username") String username,
                                   @Query("userCurrency") CurrencyValues userCurrency,
                                   @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                   @Query("from") @Nullable String from,
                                   @Query("to") @Nullable String to);

    @GET("internal/v2/spends/all")
    Call<RestPage<SpendJson>> allSpendsPageable(@Query("username") String username,
                                                @Query("filterCurrency") @Nullable CurrencyValues filterCurrency,
                                                @Query("from") @Nullable String from,
                                                @Query("to") @Nullable String to,
                                                @Query("page") @Nullable Integer page,
                                                @Query("size") @Nullable Integer size,
                                                @Query("sort") @Nullable List<String> sort);
}
