package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.model.allure.AllureResults;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AllureDockerApi {

    @POST("/allure-docker-service/send-results")
    Call<JsonNode> uploadResults(@Query("project_id") String projectId,
                                 @Body AllureResults results);

    @GET("/allure-docker-service/generate-report")
    Call<JsonNode> generateReport(@Query("project_id") String projectId,
                                  @Query("execution_name") String executionName,
                                  @Query("execution_from") String executionFrom,
                                  @Query("execution_type") String executionType);
}
