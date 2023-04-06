package niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import niffler.api.context.CookieHolder;
import niffler.api.context.SessionStorageHolder;
import niffler.api.interceptops.AddCookiesReqInterceptor;
import niffler.api.interceptops.ExtractCodeFromRespInterceptor;
import niffler.api.interceptops.ReceivedCookieRespInterceptor;
import niffler.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class NifflerAuthClient {

    private static final Config CFG = Config.getConfig();

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .followRedirects(true)
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(new ReceivedCookieRespInterceptor())
            .addNetworkInterceptor(new AddCookiesReqInterceptor())
            .addNetworkInterceptor(new ExtractCodeFromRespInterceptor())
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(CFG.authUrl())
            .build();

    private final NifflerAuthApi nifflerAuthApi = retrofit.create(NifflerAuthApi.class);

    @Step("Send REST GET('/oauth2/authorize') request to niffler-auth")
    public void authorize() throws Exception {
        SessionStorageHolder.getInstance().init();
        nifflerAuthApi.authorize(
                "code",
                "client",
                "openid",
                CFG.frontUrl() + "authorized",
                SessionStorageHolder.getInstance().getCodeChallenge(),
                "S256"
        ).execute();
    }

    @Step("Send REST POST('/login') request to niffler-auth")
    public Response<Void> login(String username, String password) throws Exception {
        return nifflerAuthApi.login(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username,
                password
        ).execute();
    }

    @Step("Send REST POST('/oauth2/token') request to niffler-auth")
    public JsonNode getToken() throws Exception {
        String basic = "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
        return nifflerAuthApi.getToken(
                basic,
                "client",
                CFG.frontUrl() + "authorized",
                "authorization_code",
                SessionStorageHolder.getInstance().getCode(),
                SessionStorageHolder.getInstance().getCodeVerifier()
        ).execute().body();
    }

    @Step("Send REST POST('/register') request to niffler-auth")
    public Response<Void> register(String username, String password) throws Exception {
        return nifflerAuthApi.register(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username,
                password,
                password
        ).execute();
    }
}
