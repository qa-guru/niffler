package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.CookieHolder;
import guru.qa.niffler.api.logging.ReceivedCookieRespAllureAppender;
import guru.qa.niffler.api.logging.ReceivedCookieRespAttachment;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class ReceivedCookieRespInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    CookieHolder loginDataHolder = CookieHolder.getInstance();
    List<String> headers = response.headers("Set-Cookie");
    List<String> storedCookies = loginDataHolder.getStoredCookies();

    for (String header : headers) {
      String[] setCookie = header.split(";");
      for (String s : setCookie) {
        if (s.contains("XSRF-TOKEN") || s.contains("JSESSIONID")) {
          String[] keyValuePair = s.split("=");
          String cookieName = keyValuePair[0];
          loginDataHolder.removeCookie(cookieName);
          if (keyValuePair.length == 2) {
            String cookieValue = keyValuePair[1];
            storedCookies.add(cookieName + "=" + cookieValue);

            new ReceivedCookieRespAllureAppender().addAttachment(
                ReceivedCookieRespAttachment.newInstance(cookieName, cookieValue));
          }
        }
      }
    }
    loginDataHolder.setCookie(storedCookies);
    return response;
  }
}
