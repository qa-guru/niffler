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

    @POST("allure-docker-service/projects")
    Call<JsonNode> createProject(@Body AllureProject project);
}
