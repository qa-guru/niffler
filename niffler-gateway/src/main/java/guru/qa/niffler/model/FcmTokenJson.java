package guru.qa.niffler.model;

import jakarta.validation.constraints.NotBlank;

public record FcmTokenJson(String username, @NotBlank String token, String userAgent) {
  public FcmTokenJson addUsername(String username) {
    return new FcmTokenJson(
        username,
        this.token,
        this.userAgent
    );
  }
}
