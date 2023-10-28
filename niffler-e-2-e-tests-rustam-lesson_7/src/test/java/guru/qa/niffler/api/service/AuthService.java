package guru.qa.niffler.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

  // empty COOKIES
  // empty SESSION STORAGE

  // 1 запрос
  // http://127.0.0.1:9000/oauth2/authorize?
  // response_type=code
  // &client_id=client
  // &scope=openid
  // &redirect_uri=http://127.0.0.1:3000/authorized
  // &code_challenge=2KRoPqMsjBUn5YjYIpt92sAMRsKLugE0Y4vIGDgwLXE
  // &code_challenge_method=S256

  @GET("/oauth2/authorize")
  Call<Void> authorize(
      @Query("response_type") String responseType,
      @Query("client_id") String clientId,
      @Query("scope") String scope,
      @Query(value = "redirect_uri", encoded = true) String redirectUri,
      @Query("code_challenge") String codeChallenge,
      @Query("code_challenge_method") String codeChallengeMethod
  );

  // 0. результат - 302 на http://127.0.0.1:9000/login + Set-Cookie: JSESSIONID=9199351590D5EE128C2CB9F0B9E3F621; Path=/
  // 1. в респонсе на http://127.0.0.1:9000/login        Set-Cookie: XSRF-TOKEN=6aba0de4-8a4a-41e4-8321-c2586be7c0c8; Path=/
  // 2. результат - cookies XSRF-TOKEN & JSESSIONID

  // 2 запрос (уходят две куки)
  // http://127.0.0.1:9000/login
  // @UrlEncoded
  // _csrf: 6aba0de4-8a4a-41e4-8321-c2586be7c0c8
  // username: rustam
  // password: 12345

  @POST("/login")
  @FormUrlEncoded
  Call<Void> login(
      @Header("Cookie") String jsessionCookie,
      @Header("Cookie") String xsrfCookie,
      @Field("_csrf") String xsrf,
      @Field("username") String username,
      @Field("password") String password
  );

  // 0. результат - 302 на http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=2KRoPqMsjBUn5YjYIpt92sAMRsKLugE0Y4vIGDgwLXE&code_challenge_method=S256&continue
  // Set-Cookie: JSESSIONID=68064B13A9EC005BA0DBF19DED024551; Path=/
  // Set-Cookie: XSRF-TOKEN=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT
  // http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=2KRoPqMsjBUn5YjYIpt92sAMRsKLugE0Y4vIGDgwLXE&code_challenge_method=S256&continue
  // 302 на http://127.0.0.1:3000/authorized?code=YrLeFdKU-ZoshyMesnnb43bx6CEZylK25M5so-B1dU6mzqusCWTAFpvKbYWIMyBaVpPEq57vZyUZnfzjfqp6tolMrqRaJqZ8xmPTZEDiGHqvx7bWlgdZwFhADl9KhcmM
  // http://127.0.0.1:3000/authorized?code=YrLeFdKU-ZoshyMesnnb43bx6CEZylK25M5so-B1dU6mzqusCWTAFpvKbYWIMyBaVpPEq57vZyUZnfzjfqp6tolMrqRaJqZ8xmPTZEDiGHqvx7bWlgdZwFhADl9KhcmM
  // 200 ОК

  // 3 запрос
  // http://127.0.0.1:9000/oauth2/token?
  // client_id=client
  // &redirect_uri=http://127.0.0.1:3000/authorized
  // &grant_type=authorization_code
  // &code=YrLeFdKU-ZoshyMesnnb43bx6CEZylK25M5so-B1dU6mzqusCWTAFpvKbYWIMyBaVpPEq57vZyUZnfzjfqp6tolMrqRaJqZ8xmPTZEDiGHqvx7bWlgdZwFhADl9KhcmM
  // &code_verifier=UO5D_6tmJJuALPGpVeJThtvECGN4Wi0mJjmK5ex2zww
  // + Authorization: Basic Y2xpZW50OnNlY3JldA==

  @POST("/oauth2/token")
  Call<JsonNode> token(
      @Header("Authorization") String authorization,
      @Query("client_id") String clientId,
      @Query(value = "redirect_uri", encoded = true) String redirectUri,
      @Query("grant_type") String grantType,
      @Query("code") String code,
      @Query("code_verifier") String codeVerifier
  );

  // результат
  // {
  //    "access_token": "eyJraWQiOiI2OWYwZWE4ZS0zMjRkLTRkN2MtYWYxOS1jMDQxODYxZDRhNjkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJydXN0YW0iLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2OTgzNjI0NTEsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJleHAiOjE2OTgzNjYwNTEsImlhdCI6MTY5ODM2MjQ1MX0.Ze1g3pD8IlikV8UhBlVctDAZ3FOy0oLpwZWnERsrX7yLVr9qIY4FC5PA6oMDvO2R1UNbozMjrUEneihCqozhSd6b5nWPP7OPOr6JehMh13vOb9dDbZUvUSWnLCtyFiZT_FwhAe05iTi-VsKx7BTYv6E3N10E4ufuOMjqoxYcLdgs1VR-hzOh0L0PbF-UqvzxERKG7gaYhJ92jYstQ7NQ4CoQ0gqkWrTVJecDPyLtWCCxU7i1y9vP0k_9vUAIYVgUsW4q3jTOf5jQ2uYzE425gcb8JZnLBJ0mxbSDpUf2uv5f91eYlSgwGMUTlgvrjXBNlyE5AjWhJA_28JYLex5KKg",
  //      "refresh_token": "ayyp338568LBfAFI-cy7k-fPQ5l-mah2f_O1k5bkVxaHVRlns2pHe6qtaq3IM5ajAKRTUrTjZstrOq6-Ru0BrPAfO9W-bG7OOknIfwdpGCatNaq6z6anAlXOk2UMgFor",
  //      "scope": "openid",
  //      "id_token": "eyJraWQiOiI2OWYwZWE4ZS0zMjRkLTRkN2MtYWYxOS1jMDQxODYxZDRhNjkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJydXN0YW0iLCJhdWQiOiJjbGllbnQiLCJhenAiOiJjbGllbnQiLCJhdXRoX3RpbWUiOjE2OTgzNjI0NTEsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6OTAwMCIsImV4cCI6MTY5ODM2NDI1MSwiaWF0IjoxNjk4MzYyNDUxLCJzaWQiOiJJY2pQNFdpVWpjSTBhLVA0RzVmWHdGY3Vtd09VOERGSHhrNHlEeC0tWk5zIn0.FoA3z5Q_kyQxsT8_EDozsdwfdT4KAnm0TPucmWwrNIWdvaGOZumL5aL6WUyUPjhxkEYBIntYOKfKPW1nV_QoAI7MwfXsttImWZ1yUpGjjMZY4EssjKzOmIumKUsstLSbTLSia-zBwauT0fGb9zxkJUv9xh4pA3NYq6dZ5pVXfRb-yJlLPJQQtVhN1GXET-Ju-QPWUREA-vBePl8dD8JZ6yB5h_wVLa-6ahJhmG5MNlavEK4kReWkUCs_rSQq8olP5PC-JLYjCG1zYxRx1oI5rNStuwg93ubf-CIJ1Hxjo8_7EREbmxSil57Qm2hZ7bPG41mRtxeCkf3UsAmrazBaaQ",
  //      "token_type": "Bearer",
  //      "expires_in": 3600
  //  }
  // в результате в браузере (Session storage)
  //  codeChallenge	2KRoPqMsjBUn5YjYIpt92sAMRsKLugE0Y4vIGDgwLXE
  //  id_token	eyJraWQiOiI2OWYwZWE4ZS0zMjRkLTRkN2MtYWYxOS1jMDQxODYxZDRhNjkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJydXN0YW0iLCJhdWQiOiJjbGllbnQiLCJhenAiOiJjbGllbnQiLCJhdXRoX3RpbWUiOjE2OTgzNjI0NTEsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6OTAwMCIsImV4cCI6MTY5ODM2NDI1MSwiaWF0IjoxNjk4MzYyNDUxLCJzaWQiOiJJY2pQNFdpVWpjSTBhLVA0RzVmWHdGY3Vtd09VOERGSHhrNHlEeC0tWk5zIn0.FoA3z5Q_kyQxsT8_EDozsdwfdT4KAnm0TPucmWwrNIWdvaGOZumL5aL6WUyUPjhxkEYBIntYOKfKPW1nV_QoAI7MwfXsttImWZ1yUpGjjMZY4EssjKzOmIumKUsstLSbTLSia-zBwauT0fGb9zxkJUv9xh4pA3NYq6dZ5pVXfRb-yJlLPJQQtVhN1GXET-Ju-QPWUREA-vBePl8dD8JZ6yB5h_wVLa-6ahJhmG5MNlavEK4kReWkUCs_rSQq8olP5PC-JLYjCG1zYxRx1oI5rNStuwg93ubf-CIJ1Hxjo8_7EREbmxSil57Qm2hZ7bPG41mRtxeCkf3UsAmrazBaaQ
  //  codeVerifier	UO5D_6tmJJuALPGpVeJThtvECGN4Wi0mJjmK5ex2zww

}
