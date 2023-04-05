package niffler.ws;

import niffler.test.ws.RequestEnvelope;
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
    Call<String> currentUserRequest(@Body RequestEnvelope user);

}
