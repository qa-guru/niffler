package niffler.service.api;

import niffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class RestUserDataClient {

    private final WebClient webClient;
    private final String nifflerUserdataBaseUri;

    @Autowired
    public RestUserDataClient(WebClient webClient,
                              @Value("${niffler-userdata.base-uri}") String nifflerUserdataBaseUri) {
        this.webClient = webClient;
        this.nifflerUserdataBaseUri = nifflerUserdataBaseUri;
    }

    public UserJson updateUserInfo(UserJson user) {
        return webClient.post()
                .uri(nifflerUserdataBaseUri + "/updateUserInfo")
                .body(Mono.just(user), UserJson.class)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }

    public UserJson currentUser(String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerUserdataBaseUri + "/currentUser").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(UserJson.class)
                .block();
    }
}
