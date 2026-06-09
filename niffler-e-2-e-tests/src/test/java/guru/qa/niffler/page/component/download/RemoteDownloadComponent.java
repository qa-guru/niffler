package guru.qa.niffler.page.component.download;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.SelenoidApiClient;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Thread.sleep;

public class RemoteDownloadComponent implements DownloadComponent {

  private final SelenoidApiClient selenoidApiClient = new SelenoidApiClient();

  @Nonnull
  @Override
  public File download(SelenideElement element, String fileName) throws IOException, InterruptedException {
    element.click();
    return downloadFromSelenoid(fileName);
  }

  @Nonnull
  private File downloadFromSelenoid(String fileName) throws IOException, InterruptedException {
    final String sessionId = WebDriverRunner.driver().getSessionId().toString();
    final Path targetFile = Files.createTempFile("selenoid-download-", "-" + fileName);
    final long deadline = System.currentTimeMillis() + Configuration.timeout;

    while (System.currentTimeMillis() < deadline) {
      byte[] content = selenoidApiClient.download(sessionId, fileName);
      if (content.length > 0) {
        Files.write(targetFile, content);
        return targetFile.toFile();
      }
      sleep(200);
    }

    throw new AssertionError("File was not downloaded from Selenoid: " + fileName);
  }
}
