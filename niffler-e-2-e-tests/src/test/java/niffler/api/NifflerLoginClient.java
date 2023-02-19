package niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import niffler.api.context.CookieHolder;
import niffler.api.interceptops.CodeInterceptor;
import niffler.api.interceptops.CookieInterceptor;
import niffler.api.interceptops.RequestInterceptor;
import niffler.api.context.SessionStorage;
import niffler.config.Config;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class NifflerLoginClient {

    Config CFG = Config.getConfig();

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .followRedirects(true)
            .addNetworkInterceptor(new CookieInterceptor())
            .addNetworkInterceptor(new RequestInterceptor())
            .addNetworkInterceptor(new CodeInterceptor())
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .baseUrl(CFG.authUrl())
            .build();

    private final NifflerLoginApi nifflerAuthApi = retrofit.create(NifflerLoginApi.class);

    public void authorize() throws Exception {
        SessionStorage.getInstance().init();
        nifflerAuthApi.authorize(
                "code",
                "client",
                "openid",
                  CFG.frontUrl() + "authorized",
                SessionStorage.getInstance().getCodeChallenge(),
                "S256"
                ).execute();
    }

    public Response<Void> login(String username, String password) throws Exception {
        return nifflerAuthApi.login(
                CookieHolder.getInstance().getCookieByPart("JSESSIONID"),
                CookieHolder.getInstance().getCookieByPart("XSRF-TOKEN"),
                CookieHolder.getInstance().getCookieValueByPart("XSRF-TOKEN"),
                username,
                password
        ).execute();
    }


    public JsonNode getToken() throws Exception {
        String basic = "Basic " + Base64.getEncoder().encodeToString("client:secret".getBytes(StandardCharsets.UTF_8));
        return nifflerAuthApi.getToken(
                basic,
                "client",
                CFG.frontUrl() + "authorized",
                "authorization_code",
                SessionStorage.getInstance().getCode(),
                SessionStorage.getInstance().getCodeVerifier()
        ).execute().body();
    }

}
