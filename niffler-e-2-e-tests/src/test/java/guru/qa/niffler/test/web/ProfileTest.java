package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.SuccessMessage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static guru.qa.niffler.utils.DataUtils.randomCategory;
import static guru.qa.niffler.utils.DataUtils.randomName;
import static guru.qa.niffler.utils.SuccessMessage.PROFILE_UPDATED;

@Epic(" [WEB][niffler-ng-client]: Профиль")
@DisplayName(" [WEB][niffler-ng-client]: Профиль")
public class ProfileTest extends BaseWebTest {

  @ScreenShotTest(expected = "cat.png")
  @AllureId("500004")
  @DisplayName("WEB: Пользователь может отредактировать все поля в профиле")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldUpdateProfileWithAllFieldsSet(BufferedImage expectedAvatar) throws Exception {
    final String newName = randomName();

    ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
        .setName(newName)
        .uploadPhotoFromClasspath("img/cat.jpeg")
        .submitProfile()
        .checkAlertMessage(PROFILE_UPDATED.content);

    Selenide.refresh();

    profilePage.checkName(newName)
        .checkPhotoExist()
        .checkPhoto(expectedAvatar);
  }

  @Test
  @AllureId("500005")
  @DisplayName("WEB: Пользователь может отредактировать профиль с заполнением только обязательных полей")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void shouldUpdateProfileWithOnlyRequiredFields() {
    final String newName = randomName();

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
    String newCategory = randomCategory();
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .addCategory(newCategory)
        .checkAlertMessage(SuccessMessage.CATEGORY_ADDED.content)
        .checkCategoryExists(newCategory);
  }

  @Test
  @AllureId("500007")
  @DisplayName("WEB: Пользователь не имеет возможности добавить более 8 категорий")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser(
      categories = {
          @GenerateCategory(name = "Food"),
          @GenerateCategory(name = "Bars"),
          @GenerateCategory(name = "Clothes"),
          @GenerateCategory(name = "Friends"),
          @GenerateCategory(name = "Music"),
          @GenerateCategory(name = "Sports"),
          @GenerateCategory(name = "Walks"),
          @GenerateCategory(name = "Books")
      }
  ))
  void shouldForbidAddingMoreThat8Categories() {
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkThatCategoryInputDisabled();
  }

  @Test
  @AllureId("500025")
  @DisplayName("WEB: Архивные категории отображаются при выбор тоггла `show archived`")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(
          name = "archived",
          archived = true
      )
  ))
  void archivedCategoryShouldPresentInCategoriesList(@User UserJson user) {
    final String categoryName = user.testData().categories().getFirst().name();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkArchivedCategoryExists(categoryName);
  }
}
