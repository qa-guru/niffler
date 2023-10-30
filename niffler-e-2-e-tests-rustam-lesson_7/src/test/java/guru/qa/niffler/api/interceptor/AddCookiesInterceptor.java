package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.CookieContext;
import java.io.IOException;
import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

  private final static String JSESSIONID = "JSESSIONID=";
  private final static String XSRF_TOKEN = "XSRF-TOKEN=";

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request originalRequest = chain.request();

    final CookieContext cookieContext = CookieContext.getInstance();
    String cookieXsrf = cookieContext.getCookie("XSRF-TOKEN");
    String jsessionId = cookieContext.getCookie("JSESSIONID");

    final String url = originalRequest.url().url().toString();
    final String method = originalRequest.method();
    if (method.equals("POST") && url.contains("http://127.0.0.1:9000/login")) {
      System.out.println("### http://127.0.0.1:9000/login Cookies will be added: " + JSESSIONID + jsessionId);
      System.out.println("### http://127.0.0.1:9000/login Cookies will be added: " + XSRF_TOKEN + cookieXsrf);
    }

    if (method.equals("GET") && url.contains("&continue")) {
      System.out.println("### Cookies will be added: " + JSESSIONID + jsessionId);
      System.out.println("### Cookies will be added: " + XSRF_TOKEN + cookieXsrf);
    }

    final Builder builder = originalRequest.headers().newBuilder();
    builder.removeAll("Cookie");
    if (jsessionId != null) {
      builder.add("Cookie", JSESSIONID + jsessionId);
    }
    if (cookieXsrf != null) {
      builder.add("Cookie", XSRF_TOKEN + cookieXsrf);
    }

    final Headers headers = builder.build();

    return chain.proceed(originalRequest.newBuilder()
        .method(originalRequest.method(), originalRequest.body())
        .headers(headers)
        .url(originalRequest.url())
        .build());
  }
}
