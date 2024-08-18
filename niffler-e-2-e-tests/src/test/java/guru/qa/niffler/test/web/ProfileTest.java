package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.SuccessMessage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.DataUtils.generateNewCategory;
import static guru.qa.niffler.utils.DataUtils.generateRandomName;
import static guru.qa.niffler.utils.SuccessMessage.PROFILE_UPDATED;

@Epic(" [WEB][niffler-ng-client]: Профиль")
@DisplayName(" [WEB][niffler-ng-client]: Профиль")
public class ProfileTest extends BaseWebTest {

  @Test
  @AllureId("500004")
  @DisplayName("WEB: Пользователь может отредактировать все поля в профиле")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldUpdateProfileWithAllFieldsSet() {
    final String newName = generateRandomName();

    ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
        .uploadPhotoFromClasspath("img/cat.jpeg")
        .setName(newName)
        .submitProfile()
        .checkAlertMessage(PROFILE_UPDATED.content);

    Selenide.refresh();

    profilePage.checkName(newName).checkPhotoExist();
  }

  @Test
  @AllureId("500005")
  @DisplayName("WEB: Пользователь может отредактировать профиль с заполнением только обязательных полей")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldUpdateProfileWithOnlyRequiredFields() {
    final String newName = generateRandomName();

    ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
        .waitForPageLoaded()
        .setName(newName)
        .submitProfile()
        .checkAlertMessage(PROFILE_UPDATED.content);

    Selenide.refresh();

    profilePage.checkName(newName);
  }

  @Test
  @AllureId("500006")
  @DisplayName("WEB: Пользователь имеет возможность добавить категорию трат")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldAddNewCategory() {
    String newCategory = generateNewCategory();
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .addCategory(newCategory)
        .checkAlertMessage(SuccessMessage.CATEGORY_ADDED.content)
        .checkCategoryExists(newCategory);
  }

  @Test
  @AllureId("500007")
  @DisplayName("WEB: Пользователь не имеет возможности добавить более 8 трат")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      categories = {
          @GenerateCategory("Food"),
          @GenerateCategory("Bars"),
          @GenerateCategory("Clothes"),
          @GenerateCategory("Friends"),
          @GenerateCategory("Music"),
          @GenerateCategory("Sports"),
          @GenerateCategory("Walks"),
          @GenerateCategory("Books")
      }
  ))
  void shouldForbidAddingMoreThat8Categories() {
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkThatCategoryInputDisabled();
  }
}
