package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
  // &code_challenge=fN_9Pve8mA4W6UhUT-4cjJ9uM6TtfshA0AYPj5OHEyk
  // &code_challenge_method=S256

  @GET("/authorize")
  Call<Void> authorize(
      @Query("response_type") String responseType,
      @Query("client_id") String clientId,
      @Query("scope") String scope,
      @Query(value = "redirect_uri", encoded = true) String redirectUri,
      @Query("code_challenge") String codeChallenge,
      @Query("code_challenge_method") String codeChallengeMethod
  );

  // 0. результат - 302 на http://127.0.0.1:9000/login + Set-Cookie: JSESSIONID=66C3703C5BBDEB8922E6764E4FF4B779; Path=/
  // 1. в респонсе на http://127.0.0.1:9000/login        Set-Cookie: XSRF-TOKEN=97491902-070b-4758-b877-83aefbf3977b; Path=/
  // 2. результат - cookies - XSRF-TOKEN & JSESSIONID

  @POST("/login")
  @FormUrlEncoded
  Call<Void> login(
      @Header("Cookie") String jsessionIdCookie,
      @Header("Cookie") String xsrfCookie,
      @Field("_csrf") String xsrf,
      @Field("username") String username,
      @Field("password") String password
  );

  // 2 запрос (уходят 2 куки)
  // http://127.0.0.1:9000/login
  // @UrlEncoded
  //  _csrf: 97491902-070b-4758-b877-83aefbf3977b
  //  username: dima
  //  password: 12345
  // 0. результат - 302 на http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=fN_9Pve8mA4W6UhUT-4cjJ9uM6TtfshA0AYPj5OHEyk&code_challenge_method=S256&continue
  // Set-Cookie: JSESSIONID=AFB5FAF3F511A65B09C18BAC2742AFED; Path=/
  // Set-Cookie: XSRF-TOKEN=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:10 GMT; Path=/
  // http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=fN_9Pve8mA4W6UhUT-4cjJ9uM6TtfshA0AYPj5OHEyk&code_challenge_method=S256&continue
  // 302 на http://127.0.0.1:3000/authorized?code=HObxU7Ujv3C0e66Wy67M_Ocr5Y4HBV44ge95fzOERkRhWD28QE22hVVpoACUN90dWNQgpD1EF5To5Dt-DvpLcagMQ2-Vo5zhpKI7EA3ygua8gslv2fK7Y6ayyr2Ii2-b
  // http://127.0.0.1:3000/authorized?code=HObxU7Ujv3C0e66Wy67M_Ocr5Y4HBV44ge95fzOERkRhWD28QE22hVVpoACUN90dWNQgpD1EF5To5Dt-DvpLcagMQ2-Vo5zhpKI7EA3ygua8gslv2fK7Y6ayyr2Ii2-b
  // 200 OK

  @POST("/oauth2/token")
  Call<JsonNode> token(
      @Header("Authorization") String authorization,
      @Query("client_id") String clientId,
      @Query(value = "redirect_uri", encoded = true) String redirectUri,
      @Query("grant_type") String grantType,
      @Query("code") String code,
      @Query("code_verifier") String codeVerifier
  );

  // 3 запрос
  // http://127.0.0.1:9000/oauth2/token?client_id=client&redirect_uri=http://127.0.0.1:3000/authorized&grant_type=authorization_code&code=HObxU7Ujv3C0e66Wy67M_Ocr5Y4HBV44ge95fzOERkRhWD28QE22hVVpoACUN90dWNQgpD1EF5To5Dt-DvpLcagMQ2-Vo5zhpKI7EA3ygua8gslv2fK7Y6ayyr2Ii2-b&code_verifier=FbDV4SQ7cKYbr1DsHUwHXZdIE-ajM7LvOZGNmHtlBN4
  // + Authorization: Basic Y2xpZW50OnNlY3JldA==
  // результат
//  {
//    "access_token": "eyJraWQiOiJkMTQ3Mzc0OC02NDIzLTQzYzItOTliNy1kYTY5NmU5N2NjYzgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJkaW1hIiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjg0OTQ4OTIxLCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo5MDAwIiwiZXhwIjoxNjg0OTg0OTIxLCJpYXQiOjE2ODQ5NDg5MjF9.TaO2XX0xpNEvlhOdsVK8oToBTE_4xGavie9tchwoLCZBVrLo8T3v2bmQdb8wszCP_Y8g85iXSgQaXiIEZ-nPAXOqt2dIU5WIHdEyPK9rcVjdtOoDoWcjq8ilwMNU7J6PeRF_HMDCJzK4wWNDWpiVQzf5Rqr6XpYDC5ok9bMmmaNokK1cvh-M4h3yQhgSDYKhMCtIsauQAM5dUhIxHVTe7gwfUiD2fa9HHjdhV6L1EAWI4t5849eY--iEnn8eXsCqkq6msqlELs5q8WO9vVOwIblGXl2higxahInR9Ev3I7WVcUtwDMGldo8jGMHoyJyRhlEsbY2-UaqvMBz9uBVY-A",
//      "refresh_token": "yJjV9mUbZSRFnoDzp6QW7GG37GIZMuJuRdjnxR2BCSJPeFl516U6Z6ZWXMoWvl2jKlQ5hfxSXOsyUBP9glyo461XNYQFH9BgU4GpxzpAai3-Btuo9x73EeGmSWrYB37F",
//      "scope": "openid",
//      "id_token": "eyJraWQiOiJkMTQ3Mzc0OC02NDIzLTQzYzItOTliNy1kYTY5NmU5N2NjYzgiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJzdWIiOiJkaW1hIiwiYXVkIjoiY2xpZW50IiwiZXhwIjoxNjg0OTUwNzIxLCJpYXQiOjE2ODQ5NDg5MjEsImF6cCI6ImNsaWVudCJ9.j4Y-WGoIrt3fZ8exmOIMA6w8mJnolRT_YwqPKL1IpTWdCgeJI80uvANx-QLEgJGWE_MuUTjx7KKSI8bZUmY84u4WraewrMc-Mtdyk0VO9tyJomUE8m2WDPTblBIcKFsyGrX5a8PkiqTSpDlY_7qAYxV84FUEswOI_5EEWTE4PwDihd3gaR3Wv2Rek4mFMAxz6Yddjq9fx2cp1_eUHDsC-1S12-RygF3n8PycSjpx0xIburg3-B69ZK3SAOPoiypyQFU4V0EFKl2eOTgvynt0VGfrsrGutFgIhSd5tuPweM1Fr72o33ccEijhwQxjS_mxKQl1VZ5b5LSmDCipGWq8SA",
//      "token_type": "Bearer",
//      "expires_in": 35999
//  }
  // в браузере (Session storage)
//  codeChallenge	fN_9Pve8mA4W6UhUT-4cjJ9uM6TtfshA0AYPj5OHEyk
//  id_token	eyJraWQiOiJkMTQ3Mzc0OC02NDIzLTQzYzItOTliNy1kYTY5NmU5N2NjYzgiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjkwMDAiLCJzdWIiOiJkaW1hIiwiYXVkIjoiY2xpZW50IiwiZXhwIjoxNjg0OTUwNzIxLCJpYXQiOjE2ODQ5NDg5MjEsImF6cCI6ImNsaWVudCJ9.j4Y-WGoIrt3fZ8exmOIMA6w8mJnolRT_YwqPKL1IpTWdCgeJI80uvANx-QLEgJGWE_MuUTjx7KKSI8bZUmY84u4WraewrMc-Mtdyk0VO9tyJomUE8m2WDPTblBIcKFsyGrX5a8PkiqTSpDlY_7qAYxV84FUEswOI_5EEWTE4PwDihd3gaR3Wv2Rek4mFMAxz6Yddjq9fx2cp1_eUHDsC-1S12-RygF3n8PycSjpx0xIburg3-B69ZK3SAOPoiypyQFU4V0EFKl2eOTgvynt0VGfrsrGutFgIhSd5tuPweM1Fr72o33ccEijhwQxjS_mxKQl1VZ5b5LSmDCipGWq8SA
//  codeVerifier	FbDV4SQ7cKYbr1DsHUwHXZdIE-ajM7LvOZGNmHtlBN4


}
