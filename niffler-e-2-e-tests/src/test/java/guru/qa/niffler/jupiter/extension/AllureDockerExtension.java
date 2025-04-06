package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.AllureDockerApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.allure.AllureResults;
import guru.qa.niffler.model.allure.DecodedAllureFile;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

public class AllureDockerExtension implements SuiteExtension {

  private static final Logger LOG = LoggerFactory.getLogger(AllureDockerExtension.class);

  private static final boolean inDocker = "docker".equals(System.getProperty("test.env"));
  private static final Base64.Encoder encoder = Base64.getEncoder();
  private static final Path allureResultsDirectory = Path.of("./niffler-e-2-e-tests/build/allure-results");
  private static final String projectId = Config.PROJECT_NAME;

  private static final AllureDockerApiClient allureDockerApiClient = new AllureDockerApiClient();

  @Override
  @SneakyThrows
  public void beforeSuite(ExtensionContext context) {
    if (inDocker) {
      allureDockerApiClient.createProjectIfNotExist(projectId);
      allureDockerApiClient.clean(projectId);
    }
  }

  @Override
  public void afterSuite() {
    if (inDocker) {
      try (Stream<Path> paths = Files.walk(allureResultsDirectory).filter(Files::isRegularFile)) {
        List<DecodedAllureFile> filesToSend = new ArrayList<>();
        for (Path allureResult : paths.toList()) {
          try (InputStream is = Files.newInputStream(allureResult)) {
            filesToSend.add(
                new DecodedAllureFile(
                    allureResult.getFileName().toString(),
                    encoder.encodeToString(is.readAllBytes())
                )
            );
          }
        }
        allureDockerApiClient.sendResultsToAllure(
            projectId,
            new AllureResults(
                filesToSend
            )
        );
        allureDockerApiClient.generateReport(
            projectId,
            System.getenv("HEAD_COMMIT_MESSAGE"),
            System.getenv("BUILD_URL"),
            System.getenv("EXECUTION_TYPE")
        );
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
