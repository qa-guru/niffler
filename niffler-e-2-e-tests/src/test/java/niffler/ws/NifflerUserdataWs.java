package niffler.ws;

import niffler.userdata.wsdl.AcceptInvitationRequest;
import niffler.userdata.wsdl.AcceptInvitationResponse;
import niffler.userdata.wsdl.AddFriendRequest;
import niffler.userdata.wsdl.AddFriendResponse;
import niffler.userdata.wsdl.AllUsersRequest;
import niffler.userdata.wsdl.AllUsersResponse;
import niffler.userdata.wsdl.CurrentUserRequest;
import niffler.userdata.wsdl.CurrentUserResponse;
import niffler.userdata.wsdl.DeclineInvitationRequest;
import niffler.userdata.wsdl.DeclineInvitationResponse;
import niffler.userdata.wsdl.FriendsRequest;
import niffler.userdata.wsdl.FriendsResponse;
import niffler.userdata.wsdl.InvitationsRequest;
import niffler.userdata.wsdl.InvitationsResponse;
import niffler.userdata.wsdl.RemoveFriendRequest;
import niffler.userdata.wsdl.RemoveFriendResponse;
import niffler.userdata.wsdl.UpdateUserInfoRequest;
import niffler.userdata.wsdl.UpdateUserInfoResponse;
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

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<FriendsResponse> friendsRequest(@Body FriendsRequest friendsRequest);

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<InvitationsResponse> invitationsRequest(@Body InvitationsRequest invitationsRequest);

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<AcceptInvitationResponse> acceptInvitationRequest(@Body AcceptInvitationRequest acceptInvitationRequest);

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<DeclineInvitationResponse> declineInvitationRequest(@Body DeclineInvitationRequest declineInvitationRequest);

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<AddFriendResponse> addFriendRequest(@Body AddFriendRequest addFriendRequest);

    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8"
    })
    @POST("/ws")
    Call<RemoveFriendResponse> removeFriendRequest(@Body RemoveFriendRequest removeFriendRequest);
}
