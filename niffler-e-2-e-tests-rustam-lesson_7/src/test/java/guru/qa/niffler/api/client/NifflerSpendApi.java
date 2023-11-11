package guru.qa.niffler.api.client;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.Spend7Json;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NifflerSpendApi {

  @POST("/addSpend")
  Call<Spend7Json> addSpend(@Body Spend7Json spend);

  @POST("/category")
  Call<CategoryJson> addCategory(@Body CategoryJson category);
}
