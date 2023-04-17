package niffler.api;

import niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendService {

  @POST("/addSpend")
  Call<SpendJson> addSpend(@Body SpendJson spend);
}
