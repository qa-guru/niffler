package niffler.ws;

import niffler.model.soap.AllUsersRequest;
import niffler.model.soap.AllUsersResponse;
import niffler.model.soap.CurrentUserRequest;
import niffler.model.soap.CurrentUserResponse;
import niffler.model.soap.UpdateUserInfoRequest;
import niffler.model.soap.UpdateUserInfoResponse;
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
