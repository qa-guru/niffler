package niffler.api;

import niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NifflerUserdataService {

    String nifflerUserdataUri = "http://127.0.0.1:8089";

    @POST("/updateUserInfo")
    Call<UserJson> updateUserInfo(@Body UserJson user);

}