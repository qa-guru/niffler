package guru.qa.niffler.ws;

import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationResponse;
import guru.qa.niffler.userdata.wsdl.AddFriendRequest;
import guru.qa.niffler.userdata.wsdl.AddFriendResponse;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersResponse;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationResponse;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;
import guru.qa.niffler.userdata.wsdl.InvitationsRequest;
import guru.qa.niffler.userdata.wsdl.InvitationsResponse;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendResponse;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoRequest;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoResponse;
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
