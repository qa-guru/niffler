package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SpendService {

  @POST("/addSpend")
  Call<SpendJson> addSpend(@Body SpendJson spend);
}
