package com.github.dtitar.api;

import com.github.dtitar.model.rest.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendService {

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);
}
