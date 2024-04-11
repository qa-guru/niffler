package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.AllureDockerApiClient;
import guru.qa.niffler.model.allure.AllureResults;
import guru.qa.niffler.model.allure.DecodedAllureFile;
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

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final String allureResultsDirectory = "./niffler-e-2-e-tests/build/allure-results";
    private static final String projectId = "niffler";

    private static final AllureDockerApiClient allureDockerApiClient = new AllureDockerApiClient();

    @Override
    public void afterAllTests() {
        if ("docker".equals(System.getProperty("test.env"))) {
            try (Stream<Path> paths = Files.walk(Path.of(allureResultsDirectory))) {
                List<Path> allureResults = paths.filter(Files::isRegularFile).toList();
                List<DecodedAllureFile> filesToSend = new ArrayList<>();
                for (Path allureResult : allureResults) {
                    try (InputStream is = Files.newInputStream(allureResult)) {
                        filesToSend.add(
                                new DecodedAllureFile(
                                        allureResult.getFileName().toString(),
                                        encoder.encodeToString(is.readAllBytes())
                                )
                        );
                    }
                }
                allureDockerApiClient.createProjectIfNotExist(projectId);
                allureDockerApiClient.sendResultsToAllure(
                        projectId,
                        new AllureResults(
                                filesToSend
                        )
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
