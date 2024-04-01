package guru.qa.niffler.api;

import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface SpendApi {

    @POST("internal/spends/add")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @GET("internal/v2/spends/all")
    Call<RestPage<SpendJson>> allSpendsPageable(@Query("username") String username,
                                                @Query("filterCurrency") CurrencyValues currency,
                                                @Query("from") String from,
                                                @Query("to") String to,
                                                @Query("page") Integer page,
                                                @Query("size") Integer size,
                                                @Query("sort") List<String> sort);

    @POST("internal/categories/add")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @GET("internal/categories/all")
    Call<List<CategoryJson>> allCategories(@Query("username") String username);
}
