package guru.qa.niffler.condition.photo;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Driver;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public final class PhotoCondition extends Condition {

  public static Condition photo(String expectedPhotoClasspath) {
    ClassLoader classLoader = PhotoCondition.class.getClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(expectedPhotoClasspath)) {
      assert is != null;
      byte[] base64Photo = Base64.getEncoder().encode(is.readAllBytes());
      return new PhotoCondition(base64Photo);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private final byte[] expectedBase64Photo;

  private PhotoCondition(byte[] expectedBase64Photo) {
    super("photo");
    this.expectedBase64Photo = expectedBase64Photo;
  }

  @Nonnull
  @Override
  public CheckResult check(Driver driver, WebElement element) {
    String imageSrc = element.getAttribute("src");
    String actualBase64Photo = StringUtils.substringAfter(imageSrc, "base64,");

    return new CheckResult(Arrays.equals(expectedBase64Photo, actualBase64Photo.getBytes()),
        actualBase64Photo);
  }
}
