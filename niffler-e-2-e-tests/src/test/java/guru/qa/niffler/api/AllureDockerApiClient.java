package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.allure.AllureProject;
import guru.qa.niffler.model.allure.AllureResults;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class AllureDockerApiClient extends RestClient {

  private static final Logger LOG = LoggerFactory.getLogger(AllureDockerApiClient.class);
  private final AllureDockerApi allureDockerApi;

  public AllureDockerApiClient() {
    super(CFG.allureDockerUrl(), HttpLoggingInterceptor.Level.NONE);
    this.allureDockerApi = retrofit.create(AllureDockerApi.class);
  }

  public void clean(String projectId) throws IOException {
    allureDockerApi.cleanResults(projectId).execute();
  }

  public void generateReport(String projectId,
                             String executionName,
                             String executionFrom,
                             String executionType) throws IOException {
    allureDockerApi.generateReport(projectId, executionName, executionFrom, executionType).execute();
  }

  public void sendResultsToAllure(String projectId, AllureResults allureResults) throws IOException {
    int code = allureDockerApi.uploadResults(
        projectId,
        allureResults
    ).execute().code();
    Assertions.assertEquals(200, code);
  }

  public void createProjectIfNotExist(String projectId) throws IOException {
    int code = allureDockerApi.project(
        projectId
    ).execute().code();
    if (code == 404) {
      code = allureDockerApi.createProject(new AllureProject(projectId)).execute().code();
      Assertions.assertEquals(201, code);
    } else {
      Assertions.assertEquals(200, code);
    }
  }
}
