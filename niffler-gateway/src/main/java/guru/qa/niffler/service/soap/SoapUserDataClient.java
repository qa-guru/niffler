package guru.qa.niffler.service.soap;

import guru.qa.niffler.ex.NoSoapResponseException;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;
import java.util.Optional;

@Component
public class SoapUserDataClient extends WebServiceGatewaySupport implements UserDataClient {

  @Override
  public @Nonnull
  UserJson currentUser(@Nonnull String username) {
    CurrentUserRequest request = new CurrentUserRequest();
    request.setUsername(username);

    UserResponse response = sendAndReceive(UserResponse.class, request);

    return UserJson.fromJaxb(response.getUser());
  }

  @Override
  public @Nonnull
  UserJson updateUserInfo(@Nonnull UserJson user) {
    UpdateUserRequest request = new UpdateUserRequest();
    request.setUser(user.toJaxbUser());

    UserResponse response = sendAndReceive(UserResponse.class, request);

    return UserJson.fromJaxb(response.getUser());
  }

  @Override
  public @Nonnull
  List<UserJson> allUsers(@Nonnull String username, @Nullable String searchQuery) {
    AllUsersRequest request = new AllUsersRequest();
    request.setUsername(username);
    request.setSearchQuery(searchQuery);

    UsersResponse response = sendAndReceive(UsersResponse.class, request);

    return response.getUser().stream().map(UserJson::fromJaxb).toList();
  }

  @Nonnull
  @Override
  public Page<UserJson> allUsersV2(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
    AllUsersPageRequest request = new AllUsersPageRequest();
    request.setUsername(username);
    request.setSearchQuery(searchQuery);
    request.setPageInfo(
        new SoapPageable(pageable).pageInfo()
    );

    UsersResponse response = sendAndReceive(UsersResponse.class, request);

    return new PageImpl<>(
        response.getUser().stream().map(UserJson::fromJaxb).toList(),
        PageRequest.of(
            response.getNumber(),
            response.getSize()
        ),
        response.getTotalElements()
    );
  }

  @Nonnull
  @Override
  public PagedModel<UserJson> allUsersV3(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
    return new PagedModel<>(
        allUsersV2(username, pageable, searchQuery)
    );
  }

  @Override
  public @Nonnull
  List<UserJson> friends(@Nonnull String username, @Nullable String searchQuery) {
    FriendsRequest request = new FriendsRequest();
    request.setUsername(username);
    request.setSearchQuery(searchQuery);

    UsersResponse response = sendAndReceive(UsersResponse.class, request);

    return response.getUser().stream().map(UserJson::fromJaxb).toList();
  }

  @Nonnull
  @Override
  public Page<UserJson> friendsV2(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
    FriendsPageRequest request = new FriendsPageRequest();
    request.setUsername(username);
    request.setSearchQuery(searchQuery);
    request.setPageInfo(
        new SoapPageable(pageable).pageInfo()
    );

    UsersResponse response = sendAndReceive(UsersResponse.class, request);

    return new PageImpl<>(
        response.getUser().stream().map(UserJson::fromJaxb).toList(),
        PageRequest.of(
            response.getNumber(),
            response.getSize()
        ),
        response.getTotalElements()
    );
  }

  @Nonnull
  @Override
  public PagedModel<UserJson> friendsV3(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
    return new PagedModel<>(
        friendsV2(username, pageable, searchQuery)
    );
  }

  @Nonnull
  @Override
  public UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {
    SendInvitationRequest request = new SendInvitationRequest();
    request.setUsername(username);
    request.setFriendToBeRequested(targetUsername);

    UserResponse response = sendAndReceive(UserResponse.class, request);

    return UserJson.fromJaxb(response.getUser());
  }

  @Nonnull
  @Override
  public UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {
    AcceptInvitationRequest request = new AcceptInvitationRequest();
    request.setUsername(username);
    request.setFriendToBeAdded(targetUsername);

    UserResponse response = sendAndReceive(UserResponse.class, request);

    return UserJson.fromJaxb(response.getUser());
  }

  @Nonnull
  @Override
  public UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) {
    DeclineInvitationRequest request = new DeclineInvitationRequest();
    request.setUsername(username);
    request.setInvitationToBeDeclined(targetUsername);

    UserResponse response = sendAndReceive(UserResponse.class, request);

    return UserJson.fromJaxb(response.getUser());
  }

  @Override
  public void removeFriend(@Nonnull String username,
                           @Nonnull String targetUsername) {
    RemoveFriendRequest request = new RemoveFriendRequest();
    request.setUsername(username);
    request.setFriendToBeRemoved(targetUsername);

    getWebServiceTemplate().marshalSendAndReceive(
        getDefaultUri(),
        request
    );
  }

  private @Nonnull <T> T sendAndReceive(@Nonnull Class<T> responseType, @Nonnull Object request) {
    return Optional.ofNullable(
        responseType.cast(getWebServiceTemplate().marshalSendAndReceive(
            getDefaultUri(),
            request
        ))
    ).orElseThrow(() -> new NoSoapResponseException(
        "No SOAP " + responseType.getSimpleName() + " is given [/" + request.getClass().getSimpleName() + " Endpoint]"
    ));
  }
}
