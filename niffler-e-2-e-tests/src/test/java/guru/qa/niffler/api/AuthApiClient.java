package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.interceptor.CodeInterceptor;
import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(
                CFG.authUrl(),
                true,
                new CodeInterceptor()
        );
        this.authApi = retrofit.create(AuthApi.class);
    }


    @Step("Perform API Oauth 2.0 authorization flow for user with username: {username}, password: {password}")
    public void login(@Nonnull ExtensionContext context,
                      @Nonnull String username,
                      @Nonnull String password) throws IOException {
        authApi.authorize(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "authorized",
                ApiLoginExtension.getCodeChallenge(context),
                "S256"
        ).execute();

        authApi.login(
                username,
                password,
                ApiLoginExtension.getCsrf()
        ).execute();

        JsonNode response = authApi.token(
                "Basic " + new String(Base64.getEncoder().encode("client:secret".getBytes(UTF_8))),
                "client",
                CFG.frontUrl() + "authorized",
                "authorization_code",
                ApiLoginExtension.getCode(context),
                ApiLoginExtension.getCodeVerifier(context)
        ).execute().body();

        ApiLoginExtension.setToken(
                context,
                response.get("id_token").asText()
        );
    }

    @Step("Perform registration for user with username: {username}, password: {password}")
    public void register(@Nonnull String username, @Nonnull String password) throws IOException {
        authApi.requestRegisterForm().execute();
        authApi.register(
                username,
                password,
                password,
                ApiLoginExtension.getCsrf()
        ).execute();
    }
}
