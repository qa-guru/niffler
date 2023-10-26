package guru.qa.niffler.service.api;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDataClient;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(prefix = "niffler-userdata", name = "client", havingValue = "rest")
public class RestUserDataClient implements UserDataClient {

    private final WebClient webClient;
    private final String nifflerUserdataBaseUri;

    @Autowired
    public RestUserDataClient(WebClient webClient,
                              @Value("${niffler-userdata.base-uri}") String nifflerUserdataBaseUri) {
        this.webClient = webClient;
        this.nifflerUserdataBaseUri = nifflerUserdataBaseUri;
    }

    @Override
    public @Nonnull
    UserJson updateUserInfo(@Nonnull UserJson user) {
        return Optional.ofNullable(
                webClient.post()
                        .uri(nifflerUserdataBaseUri + "/updateUserInfo")
                        .body(Mono.just(user), UserJson.class)
                        .retrieve()
                        .bodyToMono(UserJson.class)
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/updateUserInfo Route]"));
    }

    @Override
    public @Nonnull
    UserJson currentUser(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/currentUser").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(UserJson.class)
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/currentUser Route]"));
    }

    @Override
    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/allUsers").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/allUsers Route]"));
    }

    @Override
    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("includePending", String.valueOf(includePending));
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/friends").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/friends Route]"));
    }

    @Override
    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/invitations").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.get()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/invitations Route]"));
    }

    @Override
    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username,
                                    @Nonnull FriendJson invitation) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/acceptInvitation").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.post()
                        .uri(uri)
                        .body(Mono.just(invitation), FriendJson.class)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/acceptInvitation Route]"));
    }

    @Override
    public @Nonnull
    UserJson acceptInvitationAndReturnFriend(@Nonnull String username,
                                             @Nonnull FriendJson invitation) {
        return acceptInvitation(username, invitation).stream()
                .filter(friend -> friend.username().equals(invitation.username()))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public @Nonnull
    List<UserJson> declineInvitation(@Nonnull String username,
                                     @Nonnull FriendJson invitation) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/declineInvitation").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.post()
                        .uri(uri)
                        .body(Mono.just(invitation), FriendJson.class)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/declineInvitation Route]"));
    }

    @Override
    public @Nonnull UserJson addFriend(@Nonnull String username,
                                       @Nonnull FriendJson friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/addFriend").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.post()
                        .uri(uri)
                        .body(Mono.just(friend), FriendJson.class)
                        .retrieve()
                        .bodyToMono(UserJson.class)
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST UserJson response is given [/addFriend Route]"));
    }

    @Override
    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username,
                                @Nonnull String friendUsername) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("friendUsername", friendUsername);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/removeFriend").queryParams(params).build().toUri();

        return Optional.ofNullable(
                webClient.delete()
                        .uri(uri)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                        })
                        .block()
        ).orElseThrow(() -> new NoRestResponseException("No REST List<UserJson> response is given [/removeFriend Route]"));
    }
}
