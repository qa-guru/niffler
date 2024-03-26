package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GhApi {

  @GET("/repos/qa-guru/niffler/issues/{ISSUE_NUMBER}")
  @Headers({
      "Accept: application/vnd.github+json",
      "X-GitHub-Api-Version: 2022-11-28"
  })
  Call<JsonNode> issue(@Header("Authorization") String bearerToken,
                       @Path("ISSUE_NUMBER") String issueNumber);
}
