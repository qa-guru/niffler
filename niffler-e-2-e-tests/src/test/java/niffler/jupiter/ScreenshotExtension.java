package niffler.jupiter;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class ScreenshotExtension implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.step("Screen caused by: " + throwable.getMessage(), () -> {
                screen();
            });
        }
        throw throwable;
    }

    @Attachment(type = "img/png", fileExtension = ".png", value = "screen on fail")
    private byte[] screen() {
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
