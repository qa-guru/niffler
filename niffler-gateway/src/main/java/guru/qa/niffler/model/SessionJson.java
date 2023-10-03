package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.util.Date;

public record SessionJson(@JsonProperty("username")
                          String username,
                          @JsonProperty("username")
                          Date issuedAt,
                          @JsonProperty("username")
                          Date expiresAt) {
    public static @Nonnull SessionJson empty() {
        return new SessionJson(null, null, null);
    }
}
