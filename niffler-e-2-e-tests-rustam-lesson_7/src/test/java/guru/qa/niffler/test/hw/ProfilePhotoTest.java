package guru.qa.niffler.test.hw;

import guru.qa.niffler.jupiter.annotation.ApiLoginNew;
import guru.qa.niffler.jupiter.annotation.GenerateUserNew;
import guru.qa.niffler.page.ProfilePageRustam;
import guru.qa.niffler.test.web.other.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

public class ProfilePhotoTest extends BaseWebTest {

  private final ProfilePageRustam profilePage = new ProfilePageRustam();

  @Test
  @AllureId("4")
  @ApiLoginNew(nifflerUser = @GenerateUserNew)
  void shouldHavePhotoTest() {
    String originalPhotoClasspath = "photos/photo2.jpeg";
    profilePage.open()
        .setPhoto(originalPhotoClasspath)
        .submitProfile()
        .checkPhoto(originalPhotoClasspath);
  }
}
