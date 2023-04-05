package niffler.api;

import niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface NifflerUserdataApi {

    @GET("/currentUser")
    Call<UserJson> currentUser(@Query("username") String username);

    @POST("/updateUserInfo")
    Call<UserJson> updateUserInfo(@Body UserJson user);

    @GET("/allUsers")
    Call<List<UserJson>> allUsers(@Query("username") String username);
}
