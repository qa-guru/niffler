package guru.qa.niffler.test.web;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOHibernate;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.Allure;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@WireMockTest(httpPort = 8089)
public class LoginNewUserTest extends BaseWebTest {

//  @RegisterExtension
//  WireMockExtension wm0 = WireMockExtension.newInstance()
//          .options(WireMockConfiguration.options().port(8089))
//          .configureStaticDsl(true)
//          .build();

  private static Faker faker = new Faker();
  private NifflerUsersDAO usersDAO = new NifflerUsersDAOHibernate();
  private UserEntity dbUserEntity;

  private static final String TEST_PWD = "12345";


  @BeforeEach
  void createUserForTest() {
    dbUserEntity = new UserEntity();
    dbUserEntity.setUsername("valentin4");
    dbUserEntity.setPassword(TEST_PWD);
    dbUserEntity.setEnabled(true);
    dbUserEntity.setAccountNonExpired(true);
    dbUserEntity.setAccountNonLocked(true);
    dbUserEntity.setCredentialsNonExpired(true);
    dbUserEntity.setAuthorities(Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          ae.setUser(dbUserEntity);
          return ae;
        }
    ).toList());
    usersDAO.createUser(dbUserEntity);
  }

  @AfterEach
  void cleanUp() {
    usersDAO.removeUser(dbUserEntity);
  }

  @Test
  void loginTest() throws IOException {
    stubFor(get("/currentUser?username=valentin4")
            .willReturn(
                    aResponse()
                            .withStatus(200)
                            .withHeader("Content-type", "Application/json")
                            .withBody(
                                    "{\n" +
                                            "  \"id\": \"229fc371-2821-4795-81a5-0b26d3cd417e\",\n" +
                                            "  \"username\": \"valentin4\",\n" +
                                            "  \"firstname\": \"Valentin\",\n" +
                                            "  \"surname\": null,\n" +
                                            "  \"currency\": \"RUB\",\n" +
                                            "  \"photo\": null\n" +
                                            "}"
                            )


            ));

    Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(dbUserEntity.getUsername());
    $("input[name='password']").setValue(TEST_PWD);
    $("button[type='submit']").click();

    $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
  }

}
