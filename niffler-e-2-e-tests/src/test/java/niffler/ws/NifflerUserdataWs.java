package niffler.ws;

import niffler.model.soap.AcceptInvitationRequest;
import niffler.model.soap.AcceptInvitationResponse;
import niffler.model.soap.AddFriendRequest;
import niffler.model.soap.AddFriendResponse;
import niffler.model.soap.AllUsersRequest;
import niffler.model.soap.AllUsersResponse;
import niffler.model.soap.CurrentUserRequest;
import niffler.model.soap.CurrentUserResponse;
import niffler.model.soap.DeclineInvitationRequest;
import niffler.model.soap.DeclineInvitationResponse;
import niffler.model.soap.FriendsRequest;
import niffler.model.soap.FriendsResponse;
import niffler.model.soap.InvitationsRequest;
import niffler.model.soap.InvitationsResponse;
import niffler.model.soap.RemoveFriendRequest;
import niffler.model.soap.RemoveFriendResponse;
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
