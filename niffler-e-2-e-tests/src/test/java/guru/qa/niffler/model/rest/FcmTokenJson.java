package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record FcmTokenJson(
    @JsonProperty("username")
    String username,
    @JsonProperty("token")
    String token,
    @JsonProperty("userAgent")
    String userAgent) {
}
