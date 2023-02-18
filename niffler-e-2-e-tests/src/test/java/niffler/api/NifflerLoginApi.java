package niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import niffler.config.Config;
import niffler.config.LocalConfig;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface NifflerLoginApi {
    Config CFG = Config.getConfig();

    String nifflerFrontUri = CFG.frontUrl();
    String nifflerAuthUri = CFG.authUrl();
    @GET("/oauth2/authorize")
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
            "Accept-Encoding: gzip, deflate, br"
    })
    Call<Void> preAuthorize(
            @Query("response_type") String responseType,
            @Query("client_id") String clientId,
            @Query("scope") String scope,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("code_challenge") String codeChallenge,
            @Query("code_challenge_method") String codeChallengeMethod);


    @POST("/login")
    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
            "Accept-Encoding: gzip, deflate, br"
    })
    @FormUrlEncoded
    Call<Void> login(
            @Header("Cookie") String jsessionid,
            @Header("Cookie") String xsrf,
            @Field("_csrf") String csrf,
            @Field("username") String username,
            @Field("password") String password);

    @POST("/oauth2/token")
    Call<JsonNode> getToken(
            @Header("Authorization") String basic,
            @Query("client_id") String clientId,
            @Query(value = "redirect_uri", encoded = true) String redirectUri,
            @Query("grant_type") String grantType,
            @Query("code") String code,
            @Query("code_verifier") String codeChallenge);
}
