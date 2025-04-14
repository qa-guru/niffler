package guru.qa.niffler.service.api;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.page.PagedModelJson;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.utils.HttpQueryPaginationAndSort;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@ParametersAreNonnullByDefault
public class RestUserDataClient implements UserDataClient {

  private final RestTemplate restTemplate;
  private final String nifflerUserdataApiUri;

  @Autowired
  public RestUserDataClient(RestTemplate restTemplate,
                            @Value("${niffler-userdata.base-uri}") String nifflerUserdataBaseUri) {
    this.restTemplate = restTemplate;
    this.nifflerUserdataApiUri = nifflerUserdataBaseUri + "/internal";
  }

  @Nonnull
  @Override
  public UserJson currentUser(String username) {
    return Optional.ofNullable(
        restTemplate.getForObject(
            nifflerUserdataApiUri + "/users/current?username={username}",
            UserJson.class,
            username
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/users/current/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson updateUserInfo(UserJson user) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerUserdataApiUri + "/users/update",
            user,
            UserJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/users/update/ Route]"));
  }

  @Nonnull
  @Override
  public List<UserJson> allUsers(String username, @Nullable String searchQuery) {
    return Arrays.asList(
        Optional.ofNullable(
            restTemplate.getForObject(
                nifflerUserdataApiUri + "/users/all?username={username}&searchQuery={searchQuery}",
                UserJson[].class,
                username,
                searchQuery
            )
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson[] response is given [/users/all/ Route]"))
    );
  }

  @Nonnull
  @Override
  public Page<UserJson> allUsersV2(String username, Pageable pageable, @Nullable String searchQuery) {
    ResponseEntity<RestPage<UserJson>> response = restTemplate.exchange(
        nifflerUserdataApiUri + "/v2/users/all?username={username}&searchQuery={searchQuery}"
            + new HttpQueryPaginationAndSort(pageable),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<RestPage<UserJson>>() {
        },
        username,
        searchQuery
    );
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/users/all/ Route]"));
  }

  @Nonnull
  @Override
  public PagedModel<UserJson> allUsersV3(String username, Pageable pageable, @javax.annotation.Nullable String searchQuery) {
    final ResponseEntity<PagedModelJson<UserJson>> response = restTemplate.exchange(
        nifflerUserdataApiUri + "/v3/users/all?username={username}&searchQuery={searchQuery}"
            + new HttpQueryPaginationAndSort(pageable),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<PagedModelJson<UserJson>>() {
        },
        username,
        searchQuery
    );
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NoRestResponseException("No REST PagedModel<UserJson> response is given [/v3/users/all/ Route]"));
  }

  @Nonnull
  @Override
  public List<UserJson> friends(String username, @Nullable String searchQuery) {
    return Arrays.asList(
        Optional.ofNullable(
            restTemplate.getForObject(
                nifflerUserdataApiUri + "/friends/all?username={username}&searchQuery={searchQuery}",
                UserJson[].class,
                username,
                searchQuery
            )
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson[] response is given [/friends/all/ Route]"))
    );
  }

  @Nonnull
  @Override
  public Page<UserJson> friendsV2(String username, Pageable pageable, @Nullable String searchQuery) {
    ResponseEntity<RestPage<UserJson>> response = restTemplate.exchange(
        nifflerUserdataApiUri + "/v2/friends/all?username={username}&searchQuery={searchQuery}"
            + new HttpQueryPaginationAndSort(pageable),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<RestPage<UserJson>>() {
        },
        username,
        searchQuery
    );
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/friends/all/ Route]"));
  }

  @Nonnull
  @Override
  public PagedModel<UserJson> friendsV3(String username, Pageable pageable, @Nullable String searchQuery) {
    final ResponseEntity<PagedModelJson<UserJson>> response = restTemplate.exchange(
        nifflerUserdataApiUri + "/v3/friends/all?username={username}&searchQuery={searchQuery}"
            + new HttpQueryPaginationAndSort(pageable),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<PagedModelJson<UserJson>>() {
        },
        username,
        searchQuery
    );
    return Optional.ofNullable(response.getBody())
        .orElseThrow(() -> new NoRestResponseException("No REST PagedModel<UserJson> response is given [/v3/friends/all/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson sendInvitation(String username, String targetUsername) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerUserdataApiUri + "/invitations/send?username={username}&targetUsername={targetUsername}",
            null,
            UserJson.class,
            username,
            targetUsername
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/invitations/send/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson acceptInvitation(String username, String targetUsername) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerUserdataApiUri + "/invitations/accept?username={username}&targetUsername={targetUsername}",
            null,
            UserJson.class,
            username,
            targetUsername
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/invitations/accept/ Route]"));
  }

  @Nonnull
  @Override
  public UserJson declineInvitation(String username, String targetUsername) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerUserdataApiUri + "/invitations/decline?username={username}&targetUsername={targetUsername}",
            null,
            UserJson.class,
            username,
            targetUsername
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/invitations/decline/ Route]"));
  }

  @Override
  public void removeFriend(String username, String targetUsername) {
    restTemplate.delete(
        nifflerUserdataApiUri + "/friends/remove?username={username}&targetUsername={targetUsername}",
        username,
        targetUsername
    );
  }
}
