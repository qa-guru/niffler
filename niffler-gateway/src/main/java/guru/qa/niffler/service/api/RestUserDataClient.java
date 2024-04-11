package guru.qa.niffler.service.api;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.service.UserDataClient;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "niffler-userdata", name = "client", havingValue = "rest")
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
    public UserJson currentUser(@Nonnull String username) {
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
    public UserJson updateUserInfo(@Nonnull UserJson user) {
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
    public List<UserJson> allUsers(@Nonnull String username, @Nullable String searchQuery) {
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

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public Page<UserJson> allUsers(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        return Optional.ofNullable(
                restTemplate.getForObject(
                        nifflerUserdataApiUri + "/v2/users/all?username={username}&searchQuery={searchQuery}" +
                                "&page={page}&size={size}" + extractSort(pageable),
                        RestPage.class,
                        username,
                        searchQuery,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        ).orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/users/all/ Route]"));
    }

    @Nonnull
    @Override
    public List<UserJson> friends(@Nonnull String username, @Nullable String searchQuery) {
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

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public Page<UserJson> friends(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        return Optional.ofNullable(
                restTemplate.getForObject(
                        nifflerUserdataApiUri + "/v2/friends/all?username={username}&searchQuery={searchQuery}" +
                                "&page={page}&size={size}" + extractSort(pageable),
                        RestPage.class,
                        username,
                        searchQuery,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        ).orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/friends/all/ Route]"));
    }

    @Nonnull
    @Override
    public List<UserJson> incomeInvitations(@Nonnull String username, @Nullable String searchQuery) {
        return Arrays.asList(
                Optional.ofNullable(
                        restTemplate.getForObject(
                                nifflerUserdataApiUri + "/invitations/income?username={username}&searchQuery={searchQuery}",
                                UserJson[].class,
                                username,
                                searchQuery
                        )
                ).orElseThrow(() -> new NoRestResponseException("No REST UserJson[] response is given [/invitations/income/ Route]"))
        );
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public Page<UserJson> incomeInvitations(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        return Optional.ofNullable(
                restTemplate.getForObject(
                        nifflerUserdataApiUri + "/v2/invitations/income?username={username}&searchQuery={searchQuery}" +
                                "&page={page}&size={size}" + extractSort(pageable),
                        RestPage.class,
                        username,
                        searchQuery,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        ).orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/invitations/income/ Route]"));
    }

    @Nonnull
    @Override
    public List<UserJson> outcomeInvitations(@Nonnull String username, @Nullable String searchQuery) {
        return Arrays.asList(
                Optional.ofNullable(
                        restTemplate.getForObject(
                                nifflerUserdataApiUri + "/invitations/outcome?username={username}&searchQuery={searchQuery}",
                                UserJson[].class,
                                username,
                                searchQuery
                        )
                ).orElseThrow(() -> new NoRestResponseException("No REST UserJson[] response is given [/invitations/outcome/ Route]"))
        );
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public Page<UserJson> outcomeInvitations(@Nonnull String username, @Nonnull Pageable pageable, @Nullable String searchQuery) {
        return Optional.ofNullable(
                restTemplate.getForObject(
                        nifflerUserdataApiUri + "/v2/invitations/outcome?username={username}&searchQuery={searchQuery}" +
                                "&page={page}&size={size}" + extractSort(pageable),
                        RestPage.class,
                        username,
                        searchQuery,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )
        ).orElseThrow(() -> new NoRestResponseException("No REST Page<UserJson> response is given [/v2/invitations/outcome/ Route]"));
    }

    @Nonnull
    @Override
    public UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {
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
    public UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {
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
    public UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) {
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
    public void removeFriend(@Nonnull String username, @Nonnull String targetUsername) {
        restTemplate.delete(
                nifflerUserdataApiUri + "/friends/remove?username={username}&targetUsername={targetUsername}",
                username,
                targetUsername
        );
    }

    private @Nonnull String extractSort(@Nonnull Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            StringBuilder sortQuery = new StringBuilder();
            for (Sort.Order order : pageable.getSort()) {
                sortQuery.append("&sort=");
                sortQuery.append(order.getProperty());
                sortQuery.append(",");
                sortQuery.append(order.getDirection().name());
            }
            return sortQuery.toString();
        }
        return "";
    }
}
