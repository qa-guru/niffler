package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.ByteArrayInputStream;

@ParametersAreNonnullByDefault
public class BrowserExtension implements
    LifecycleMethodExecutionExceptionHandler,
    TestWatcher,
    AfterEachCallback {

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    doScreen();
  }

  @Override
  public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreen();
    throw throwable;
  }

  @Override
  public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreen();
    throw throwable;
  }

  @Override
  public void afterEach(ExtensionContext context) {
    if (WebDriverRunner.hasWebDriverStarted()) {
      try {
        if (context.getExecutionException().isPresent()) {
          doScreen();
        }
      } finally {
        Selenide.closeWebDriver();
      }
    }
  }

  static void doScreen() {
    if (WebDriverRunner.hasWebDriverStarted()) {
      Allure.addAttachment("Screenshot on fail",
          new ByteArrayInputStream(((TakesScreenshot) WebDriverRunner.getWebDriver())
              .getScreenshotAs(OutputType.BYTES)));
    }
  }
}
