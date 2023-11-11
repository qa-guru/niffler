package guru.qa.niffler.api.service;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DBUser7Api {

  @GET("/currentUser")
  Call<UserJson> getCurrentUser(@Query("username") String username);

}
