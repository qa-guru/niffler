package guru.qa.niffler.model.gql;

import jakarta.validation.constraints.NotBlank;

public record FcmTokenGqlInput(@NotBlank String token, String userAgent) {
}
