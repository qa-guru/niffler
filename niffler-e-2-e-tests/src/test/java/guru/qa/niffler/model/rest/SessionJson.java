package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record SessionJson(@JsonProperty("username")
                          String username,
                          @JsonProperty("issuedAt")
                          Date issuedAt,
                          @JsonProperty("expiresAt")
                          Date expiresAt) {

}
