package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.service.RestClient;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class GhApiClient extends RestClient {

  private final GhApi ghApi;

  public GhApiClient() {
    super("https://api.github.com/");
    this.ghApi = retrofit.create(GhApi.class);
  }

  @Step("Get issue state by number: {issueNumber}")
  public String getIssueState(String issueNumber) throws IOException {
    JsonNode responseBody = ghApi.issue(
        "Bearer " + System.getenv("GITHUB_TOKEN"),
        issueNumber
    ).execute().body();
    return requireNonNull(responseBody).get("state").asText();
  }
}
