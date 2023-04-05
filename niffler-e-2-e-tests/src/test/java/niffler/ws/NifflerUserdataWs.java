package niffler.ws;

import niffler.ws.model.wsdl.AllUsersRequest;
import niffler.ws.model.wsdl.AllUsersResponse;
import niffler.ws.model.wsdl.CurrentUserRequest;
import niffler.ws.model.wsdl.CurrentUserResponse;
import niffler.ws.model.wsdl.UpdateUserInfoRequest;
import niffler.ws.model.wsdl.UpdateUserInfoResponse;
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
    Call<CurrentUserResponse> currentUserRequest(@Body CurrentUserRequest currentUserRequest);

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<UpdateUserInfoResponse> updateUserInfoRequest(@Body UpdateUserInfoRequest updateUserInfoRequest);

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<AllUsersResponse> allUsersRequest(@Body AllUsersRequest allUsersRequest);
}
