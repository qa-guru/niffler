package niffler.api;

import niffler.config.Config;
import niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NifflerUserdataApi {

    Config CFG =  Config.getConfig();

    String nifflerUserdataUri = CFG.userdataUrl();

    @POST("/updateUserInfo")
    Call<UserJson> updateUserInfo(@Body UserJson spend);

}
