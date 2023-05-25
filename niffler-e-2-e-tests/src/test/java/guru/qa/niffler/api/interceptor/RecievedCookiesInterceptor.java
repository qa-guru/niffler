package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.CookieContext;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

public class RecievedCookiesInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    String setCookie = response.header("Set-Cookie");

    if (setCookie != null) {
      String[] cookies = setCookie.split(";");
      for (String cookie : cookies) {
        if (cookie.contains("XSRF-TOKEN") || cookie.contains("JSESSIONID")) {
          String[] res = cookie.split("=");
          if (res.length == 2) {
            CookieContext.getInstance().setCookie(res[0], res[1]);
          } else {
            CookieContext.getInstance().removeCookie(res[0]);
          }
        }
      }
    }
    return response;
  }
}
