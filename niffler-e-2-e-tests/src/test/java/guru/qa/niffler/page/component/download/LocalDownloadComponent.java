package guru.qa.niffler.page.component.download;

import com.codeborne.selenide.DownloadOptions;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class LocalDownloadComponent implements DownloadComponent {

  @Nonnull
  @Override
  public File download(SelenideElement element, String fileName) throws IOException {
    return element.download(
        DownloadOptions.using(FileDownloadMode.FOLDER).withName(fileName)
    );
  }
}
