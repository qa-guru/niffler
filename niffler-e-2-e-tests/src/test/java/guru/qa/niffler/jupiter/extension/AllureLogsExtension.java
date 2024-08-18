package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static io.qameta.allure.model.Status.PASSED;

public class AllureLogsExtension implements SuiteExtension {

  public static final String caseName = "logs";

  @SneakyThrows
  @Override
  public void afterSuite() {
    String caseId = UUID.randomUUID().toString();
    AllureLifecycle lifecycle = Allure.getLifecycle();
    lifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName).setStatus(PASSED));
    lifecycle.startTestCase(caseId);

    lifecycle.addAttachment("auth log", "text/html", ".log", Files.newInputStream(
        Path.of("./auth.log")));
    lifecycle.addAttachment("currency log", "text/html", ".log", Files.newInputStream(
        Path.of("./currency.log")));
    lifecycle.addAttachment("gateway log", "text/html", ".log", Files.newInputStream(
        Path.of("./gateway.log")));
    lifecycle.addAttachment("spend log", "text/html", ".log", Files.newInputStream(
        Path.of("./spend.log")));
    lifecycle.addAttachment("userdata log", "text/html", ".log", Files.newInputStream(
        Path.of("./userdata.log")));

    lifecycle.startTestCase(caseId);
    lifecycle.writeTestCase(caseId);
  }
}
