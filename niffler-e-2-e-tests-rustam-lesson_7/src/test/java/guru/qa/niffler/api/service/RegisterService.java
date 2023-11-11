package guru.qa.niffler.api.service;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Field;

public interface RegisterService {

  // empty COOKIES
  // empty SESSION STORAGE

  // 1 запрос
  // GET
  // http://127.0.0.1:9000/register

  @GET("/register")
  Call<Void> requestRegisterForm();

  // 1 результат Set-Cookie: XSRF-TOKEN=ead6efc4-9cfa-4927-abdb-c4636a910b0c; Path=/

  // 2 запрос
  // POST
  // http://127.0.0.1:9000/register
  // _csrf: ead6efc4-9cfa-4927-abdb-c4636a910b0c
  // username: test3
  // password: 12345
  // passwordSubmit: 12345

  @POST("/register")
  @FormUrlEncoded
  Call<Void> register(
      @Field("username") String username,
      @Field("password") String password,
      @Field("passwordSubmit") String passwordSubmit,
      @Field("_csrf") String csrf
  );

  // 1 результат
  // 201 created

  // 3 запрос
  // GET
  // http://127.0.0.1:3000/redirect
  // XSRF-TOKEN=b826b110-7ba1-46f9-a3bb-b2e76dfcfbed

  // 1 результат
  // 200 ОК

  // 4 запрос
  // GET
  // http://127.0.0.1:9000/oauth2/authorize?response_type=code
  // &client_id=client
  // &scope=openid
  // &redirect_uri=http://127.0.0.1:3000/authorized
  // &code_challenge=4Fyt6gY3UJPoNkoGRPqpExYAkXN4hSqE4SNNAx7ZWx4
  // &code_challenge_method=S256

  // 1 результат Set-Cookie: JSESSIONID=4753261D32C2080FE0F9E0AAFFD2FC27; Path=/
  // и 302 на Location: http://127.0.0.1:9000/login
  // 2 результат GET http://127.0.0.1:9000/login
  // XSRF-TOKEN=b826b110-7ba1-46f9-a3bb-b2e76dfcfbed; JSESSIONID=4753261D32C2080FE0F9E0AAFFD2FC27

  // 5 запрос
  // http://127.0.0.1:9000/login
  // POST
  // _csrf: b826b110-7ba1-46f9-a3bb-b2e76dfcfbed
  // username: test4
  // password: 12345

  // 1 результат
  // Location:
  // http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=4Fyt6gY3UJPoNkoGRPqpExYAkXN4hSqE4SNNAx7ZWx4&code_challenge_method=S256&continue
  // Set-Cookie: JSESSIONID=099753C9C74D02DDDBBAD0FBC318C596; Path=/
  // Set-Cookie: XSRF-TOKEN=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT
  // http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http://127.0.0.1:3000/authorized&code_challenge=4Fyt6gY3UJPoNkoGRPqpExYAkXN4hSqE4SNNAx7ZWx4&code_challenge_method=S256&continue
  // 302 на
  // Location:
  // http://127.0.0.1:3000/authorized?code=UrlhmGUX3n3Zcu1uQPrXBWduL7ZyPHueGwzjTpzH0pJEXyajVb0AjcbdwwM7b4mM3UBgwgmJp9F66vRdMpaAT8oM5xaGpMOkv7Zf9MzbUrs37OcSQ5KIH-3-kil5K9ZI
  // response_type: code
  // client_id: client
  // scope: openid
  // redirect_uri: http://127.0.0.1:3000/authorized
  // code_challenge: 4Fyt6gY3UJPoNkoGRPqpExYAkXN4hSqE4SNNAx7ZWx4
  // code_challenge_method: S256
  // continue:
  // http://127.0.0.1:3000/authorized?code=UrlhmGUX3n3Zcu1uQPrXBWduL7ZyPHueGwzjTpzH0pJEXyajVb0AjcbdwwM7b4mM3UBgwgmJp9F66vRdMpaAT8oM5xaGpMOkv7Zf9MzbUrs37OcSQ5KIH-3-kil5K9ZI
  // 200 ОК

}
