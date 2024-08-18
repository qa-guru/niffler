package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.model.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static guru.qa.niffler.jupiter.extension.AllureLogsExtension.caseName;

public class AllurePassedAttachmentsExtension implements SuiteExtension {

  private static final ObjectMapper om = new ObjectMapper();
  private final String pathToAllureResults = "./niffler-e-2-e-tests/build/allure-results";

  @Override
  public void afterSuite() {
    try (Stream<Path> files = Files.walk(Path.of(pathToAllureResults))) {
      List<Path> allureResults = files
          .filter(Files::isRegularFile)
          .filter(f -> f.getFileName().toString().endsWith("-result.json"))
          .toList();

      for (Path allureResult : allureResults) {
        JsonNode jsonResult = om.readTree(Files.newInputStream(allureResult));
        if (jsonResult.get("status").asText().equals(Status.PASSED.value())
            && !jsonResult.get("testCaseName").asText().equals(caseName)) {
          ((ObjectNode) jsonResult).putArray("attachments");
          Files.write(
              Path.of(pathToAllureResults + "/" + jsonResult.get("uuid").asText() + "-result.json"),
              om.writeValueAsBytes(jsonResult)
          );
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private ObjectNode createAttachment(String filename) throws Exception {
    String sourceName = UUID.randomUUID() + "-attachment";
    ObjectNode attachment = om.createObjectNode();
    attachment.put("name", filename);
    attachment.put("source", sourceName);

    Files.copy(
        Path.of("./" + filename),
        Path.of(pathToAllureResults + "/" + attachment.get("source").asText())
    );
    return attachment;
  }
}
