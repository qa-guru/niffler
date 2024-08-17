package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.model.allure.AllureProject;
import guru.qa.niffler.model.allure.AllureResults;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AllureDockerApi {

  @POST("allure-docker-service/send-results")
  Call<JsonNode> uploadResults(@Query("project_id") String projectId,
                               @Body AllureResults results);

  @GET("allure-docker-service/projects/{project_id}")
  Call<JsonNode> project(@Path("project_id") String projectId);

  @GET("allure-docker-service/clean-results")
  Call<JsonNode> cleanResults(@Query("project_id") String projectId);

  @GET("allure-docker-service/generate-report")
  Call<JsonNode> generateReport(@Query("project_id") String projectId,
                                @Query("execution_name") String executionName,
                                @Query(value = "execution_from", encoded = true) String executionFrom,
                                @Query("execution_type") String executionType);

  @POST("allure-docker-service/projects")
  Call<JsonNode> createProject(@Body AllureProject project);
}
