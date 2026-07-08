package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.FcmTokenJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[REST][niffler-gateway]: Push Notifications")
@DisplayName("[REST][niffler-gateway]: Push Notifications")
@ParametersAreNonnullByDefault
public class GatewayPushRestTest extends BaseRestTest {

  @Test
  @AllureId("200043")
  @DisplayName("REST: POST /api/push/token регистрирует FCM токен без ошибок")
  @Tag("REST")
  @ApiLogin(user = @GenerateUser)
  void registerPushTokenTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final FcmTokenJson fcmToken = new FcmTokenJson(
        user.username(),
        UUID.randomUUID().toString(),
        "Mozilla/5.0 (test)"
    );

    step("Register FCM token — expect no exception", () ->
        assertDoesNotThrow(() -> gatewayApiClient.registerPushToken(bearerToken, fcmToken))
    );
  }
}
