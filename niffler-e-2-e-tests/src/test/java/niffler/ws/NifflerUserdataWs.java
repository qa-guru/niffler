package niffler.ws;

import niffler.ws.model.wsdl.CurrentUserRequest;
import niffler.ws.model.wsdl.CurrentUserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NifflerUserdataWs {

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<CurrentUserResponse> currentUserRequest(@Body CurrentUserRequest user);

}
