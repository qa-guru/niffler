package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.allure.AllureResults;

import java.io.IOException;

public class AllureDockerApiClient extends RestClient {

    private final AllureDockerApi allureDockerApi;

    public AllureDockerApiClient() {
        super("http://localhost:5050");
        this.allureDockerApi = retrofit.create(AllureDockerApi.class);
    }

    public void sendResultsToAllure(String projectId, AllureResults allureResults) throws IOException {
        allureDockerApi.uploadResults(
                projectId,
                allureResults
        ).execute();
    }

    public String generateReport(String projectId,
                                 String executionName,
                                 String executionFrom,
                                 String executionType) throws IOException {
        JsonNode response = allureDockerApi.generateReport(
                        projectId,
                        executionName,
                        executionFrom,
                        executionType
                ).execute()
                .body();
        return response.get("data").get("report_url").asText();
    }
}
