package guru.qa.niffler.service.soap;

import guru.qa.niffler.ex.NoSoapResponseException;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersPageRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.Direction;
import guru.qa.niffler.userdata.wsdl.FriendsPageRequest;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.IncomeInvitationsPageRequest;
import guru.qa.niffler.userdata.wsdl.IncomeInvitationsRequest;
import guru.qa.niffler.userdata.wsdl.OutcomeInvitationsPageRequest;
import guru.qa.niffler.userdata.wsdl.OutcomeInvitationsRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.SendInvitationRequest;
import guru.qa.niffler.userdata.wsdl.UpdateUserRequest;
import guru.qa.niffler.userdata.wsdl.UserResponse;
import guru.qa.niffler.userdata.wsdl.UsersResponse;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "niffler-userdata", name = "client", havingValue = "soap")
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
    public Page<UserJson> allUsers(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        AllUsersPageRequest request = new AllUsersPageRequest();
        request.setUsername(username);
        request.setSearchQuery(searchQuery);
        request.setPage(pageable.getPageNumber());
        request.setSize(pageable.getPageSize());
        request.getSort().addAll(extractSort(pageable));

        UsersResponse response = sendAndReceive(UsersResponse.class, request);

        return new PageImpl<>(
                response.getUser().stream().map(UserJson::fromJaxb).toList(),
                pageable,
                response.getTotalElements()
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
    public Page<UserJson> friends(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        FriendsPageRequest request = new FriendsPageRequest();
        request.setUsername(username);
        request.setSearchQuery(searchQuery);
        request.setPage(pageable.getPageNumber());
        request.setSize(pageable.getPageSize());
        request.getSort().addAll(extractSort(pageable));

        UsersResponse response = sendAndReceive(UsersResponse.class, request);

        return new PageImpl<>(
                response.getUser().stream().map(UserJson::fromJaxb).toList(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                response.getTotalElements()
        );
    }

    @Override
    public @Nonnull
    List<UserJson> incomeInvitations(@Nonnull String username, @Nullable String searchQuery) {
        IncomeInvitationsRequest request = new IncomeInvitationsRequest();
        request.setUsername(username);
        request.setSearchQuery(searchQuery);

        UsersResponse response = sendAndReceive(UsersResponse.class, request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Nonnull
    @Override
    public Page<UserJson> incomeInvitations(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        IncomeInvitationsPageRequest request = new IncomeInvitationsPageRequest();
        request.setUsername(username);
        request.setSearchQuery(searchQuery);
        request.setPage(pageable.getPageNumber());
        request.setSize(pageable.getPageSize());
        request.getSort().addAll(extractSort(pageable));

        UsersResponse response = sendAndReceive(UsersResponse.class, request);

        return new PageImpl<>(
                response.getUser().stream().map(UserJson::fromJaxb).toList(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                response.getTotalElements()
        );
    }

    @Override
    public @Nonnull
    List<UserJson> outcomeInvitations(@Nonnull String username, @Nullable String searchQuery) {
        OutcomeInvitationsRequest request = new OutcomeInvitationsRequest();
        request.setUsername(username);
        request.setSearchQuery(searchQuery);

        UsersResponse response = sendAndReceive(UsersResponse.class, request);

        return response.getUser().stream().map(UserJson::fromJaxb).toList();
    }

    @Nonnull
    @Override
    public Page<UserJson> outcomeInvitations(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        OutcomeInvitationsPageRequest request = new OutcomeInvitationsPageRequest();
        request.setUsername(username);
        request.setSearchQuery(searchQuery);
        request.setPage(pageable.getPageNumber());
        request.setSize(pageable.getPageSize());
        request.getSort().addAll(extractSort(pageable));

        UsersResponse response = sendAndReceive(UsersResponse.class, request);

        return new PageImpl<>(
                response.getUser().stream().map(UserJson::fromJaxb).toList(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                response.getTotalElements()
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

    private @Nonnull List<guru.qa.niffler.userdata.wsdl.Sort> extractSort(@Nonnull Pageable pageable) {
        List<guru.qa.niffler.userdata.wsdl.Sort> result = new ArrayList<>();
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                guru.qa.niffler.userdata.wsdl.Sort sort = new guru.qa.niffler.userdata.wsdl.Sort();
                sort.setProperty(order.getProperty());
                sort.setDirection(Direction.valueOf(order.getDirection().name()));
                result.add(sort);
            }
        }
        return result;
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
