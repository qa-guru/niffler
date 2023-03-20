package niffler.service.api;

import jakarta.annotation.Nonnull;
import niffler.model.FriendJson;
import niffler.model.UserJson;
import niffler.service.UserDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
@Qualifier("rest")
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
        return webClient.post()
                .uri(nifflerUserdataBaseUri + "/updateUserInfo")
                .body(Mono.just(user), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    @Override
    public @Nonnull
    UserJson currentUser(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/currentUser").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    @Override
    public @Nonnull
    List<UserJson> allUsers(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/allUsers").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    @Override
    public @Nonnull
    List<UserJson> friends(@Nonnull String username, boolean includePending) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("includePending", String.valueOf(includePending));
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/friends").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    @Override
    public @Nonnull
    List<UserJson> invitations(@Nonnull String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/invitations").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    @Override
    public @Nonnull
    List<UserJson> acceptInvitation(@Nonnull String username,
                                    @Nonnull FriendJson invitation) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/acceptInvitation").queryParams(params).build().toUri();

        return webClient.post()
                .uri(uri)
                .body(Mono.just(invitation), FriendJson.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    @Override
    public @Nonnull
    UserJson acceptInvitationAndReturnFriend(@Nonnull String username,
                                             @Nonnull FriendJson invitation) {
        return acceptInvitation(username, invitation).stream()
                .filter(friend -> friend.getUsername().equals(invitation.getUsername()))
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

        return webClient.post()
                .uri(uri)
                .body(Mono.just(invitation), FriendJson.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }

    @Override
    public UserJson addFriend(@Nonnull String username,
                              @Nonnull FriendJson friend) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/addFriend").queryParams(params).build().toUri();

        return webClient.post()
                .uri(uri)
                .body(Mono.just(friend), FriendJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    @Override
    public @Nonnull
    List<UserJson> removeFriend(@Nonnull String username,
                                @Nonnull String friendUsername) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("friendUsername", friendUsername);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/removeFriend").queryParams(params).build().toUri();

        return webClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserJson>>() {
                })
                .block();
    }
}
