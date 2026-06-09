package guru.qa.niffler.page.component.download;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public interface DownloadComponent {

  @Nonnull
  static DownloadComponent getInstance() {
    if ("docker".equals(System.getProperty("test.env"))) {
      return new RemoteDownloadComponent();
    } else if ("local".equals(System.getProperty("test.env"))) {
      return new LocalDownloadComponent();
    } else {
      throw new IllegalStateException();
    }
  }

  @Nonnull
  File download(SelenideElement element, String fileName) throws IOException, InterruptedException;
}
