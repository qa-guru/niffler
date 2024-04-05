package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ServerLogsExtension implements SuiteExtension {

    private final String pathToAllureResults = "./niffler-e-2-e-tests/build/allure-results";
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void afterAllTests() {
        try (Stream<Path> paths = Files.walk(Path.of(pathToAllureResults))) {
            List<Path> allureResults = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith("-result.json"))
                    .toList();

            for (Path allureResult : allureResults) {
                try (InputStream is = Files.newInputStream(allureResult)) {
                    JsonNode resultAsJson = om.reader().readTree(is.readAllBytes());
                    if (resultAsJson.get("testCaseName").asText().equals("logTest()")) {
                        ObjectNode authAttachment = createAttachment("auth.log");
                        ObjectNode currencyAttachment = createAttachment("currency.log");
                        ObjectNode gatewayAttachment = createAttachment("gateway.log");
                        ObjectNode spendAttachment = createAttachment("spend.log");
                        ObjectNode userdataAttachment = createAttachment("userdata.log");

                        ((ObjectNode) resultAsJson).putArray("attachments")
                                .add(authAttachment)
                                .add(currencyAttachment)
                                .add(gatewayAttachment)
                                .add(spendAttachment)
                                .add(userdataAttachment);

                        Files.write(allureResult, om.writeValueAsBytes(resultAsJson));
                        break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private ObjectNode createAttachment(String logName) throws Exception {
        String sourceName = UUID.randomUUID() + "-attachment";
        ObjectNode attachment = om.createObjectNode();
        attachment.put("name", logName);
        attachment.put("source", sourceName);

        Files.copy(
                Path.of("./" + logName),
                Path.of(pathToAllureResults + "/" + attachment.get("source").asText())
        );
        return attachment;
    }
}
