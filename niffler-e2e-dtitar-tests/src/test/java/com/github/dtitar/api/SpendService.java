package com.github.dtitar.api;

import com.github.dtitar.model.rest.CategoryJson;
import com.github.dtitar.model.rest.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface SpendService {

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);

    @POST("/category")
    Call<Void> addCategory(@Body CategoryJson category);

    @GET("/spends")
    Call<List<SpendJson>> getSpends(@Query("username") String username);

    @DELETE("/deleteSpends")
    Call<Void> deleteSpends(@Query("username") String username, @Query("ids") List<String> ids);
}
