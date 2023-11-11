package guru.qa.niffler.api.service;

import guru.qa.niffler.model.Spend7Json;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Spend7Service {

    @POST("/addSpend")
    Call<Spend7Json> addSpend(@Body Spend7Json spend);
}
