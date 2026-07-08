package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.SessionJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[REST][niffler-gateway]: Session")
@DisplayName("[REST][niffler-gateway]: Session")
@ParametersAreNonnullByDefault
public class GatewaySessionRestTest extends BaseRestTest {

  @Test
  @AllureId("200042")
  @DisplayName("REST: GET /api/session/current возвращает данные текущей сессии")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser)
  void currentSessionReturnsValidDataTest(@User UserJson user,
                                          @Token String bearerToken) throws Exception {
    final SessionJson session = gatewayApiClient.currentSession(bearerToken);

    step("Check that session is not null", () ->
        assertNotNull(session)
    );
    step("Check that username matches", () ->
        assertEquals(user.username(), session.username())
    );
    step("Check that issuedAt is not null", () ->
        assertNotNull(session.issuedAt())
    );
    step("Check that expiresAt is not null", () ->
        assertNotNull(session.expiresAt())
    );
    step("Check that expiresAt is after issuedAt", () ->
        assertTrue(session.expiresAt().after(session.issuedAt()))
    );
  }
}
