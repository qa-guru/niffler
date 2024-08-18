package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Repository;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.Selector.NESTED;

@Epic(" [WEB][niffler-ng-client]: Профиль, DB preconditions")
@DisplayName(" [WEB][niffler-ng-client]: Профиль, DB preconditions")
public class ProfileDbTest extends BaseWebTest {

  @Repository
  private UserRepository userRepository;

  private static final String TEST_FULLNAME = "Test fullname";

  @BeforeEach
  void changeCurrencyBeforeTest(@User(selector = NESTED) UserJson user) {
    UserEntity testUserFromUserdata = userRepository.findByIdInUserdata(user.id()).orElseThrow();
    testUserFromUserdata.setFullname(TEST_FULLNAME);
    userRepository.updateInUserdata(testUserFromUserdata);
  }

  @Test
  @AllureId("500003")
  @DisplayName("WEB: В профиле должна отображаться валюта, сохраненная в niffler-userdata")
  @Tag("WEB")
  @ApiLogin(user = @GenerateUser)
  void checkCurrencyTest() {
    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkName(TEST_FULLNAME);
  }
}
