package niffler.api;

import niffler.model.rest.CategoryJson;
import niffler.model.rest.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NifflerSpendApi {

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @POST("/category")
    Call<CategoryJson> addCategory(@Body CategoryJson category);
}
