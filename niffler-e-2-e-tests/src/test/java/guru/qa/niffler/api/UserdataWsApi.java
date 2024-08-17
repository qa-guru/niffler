package guru.qa.niffler.api;

import jaxb.userdata.AcceptInvitationRequest;
import jaxb.userdata.AllUsersPageRequest;
import jaxb.userdata.AllUsersRequest;
import jaxb.userdata.CurrentUserRequest;
import jaxb.userdata.DeclineInvitationRequest;
import jaxb.userdata.FriendsPageRequest;
import jaxb.userdata.FriendsRequest;
import jaxb.userdata.RemoveFriendRequest;
import jaxb.userdata.SendInvitationRequest;
import jaxb.userdata.UpdateUserRequest;
import jaxb.userdata.UserResponse;
import jaxb.userdata.UsersResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserdataWsApi {

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> currentUserRequest(@Body CurrentUserRequest currentUserRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> updateUserRequest(@Body UpdateUserRequest updateUserRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> allUsersRequest(@Body AllUsersRequest allUsersRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> allUsersPageRequest(@Body AllUsersPageRequest allUsersPageRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> friendsRequest(@Body FriendsRequest friendsRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UsersResponse> friendsPageRequest(@Body FriendsPageRequest friendsPageRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<Void> removeFriendRequest(@Body RemoveFriendRequest removeFriendRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> acceptInvitationRequest(@Body AcceptInvitationRequest acceptInvitationRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> declineInvitationRequest(@Body DeclineInvitationRequest declineInvitationRequest);

  @Headers({
      "Content-Type: text/xml",
      "Accept-Charset: utf-8"
  })
  @POST("ws")
  Call<UserResponse> sendInvitationRequest(@Body SendInvitationRequest sendInvitationRequest);
}
