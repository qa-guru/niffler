package guru.qa.niffler.api.client;

import guru.qa.niffler.api.context.CookieContext;
import guru.qa.niffler.api.interceptor.AddCookiesInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCodeInterceptor;
import guru.qa.niffler.api.interceptor.RecievedCookiesInterceptor;
import guru.qa.niffler.api.service.RegisterService;
import java.io.IOException;

public class RegisterClient extends BaseRestClient7 {

  public RegisterClient() {
    super(
        CFG.getAuthUrl(),
        true,
        new RecievedCookiesInterceptor(),
        new AddCookiesInterceptor(),
        new RecievedCodeInterceptor()
    );
  }

  private final RegisterService registerService = retrofit.create(RegisterService.class);

  public void registerPreRequest() {
    try {
      registerService.requestRegisterForm().execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void register(String username, String password, String passwordSubmit) {
    try {
      registerService.register(
          username,
          password,
          passwordSubmit,
          CookieContext.getInstance().getCookie("XSRF-TOKEN")
      ).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
