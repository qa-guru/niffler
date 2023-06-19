package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.CookieContext;
import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

import java.io.IOException;

public class AddCookiesInterceptor implements Interceptor {

    @Override
    @EverythingIsNonNull
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        final CookieContext cookieContext = CookieContext.getInstance();
        String cookieXsrf = cookieContext.getCookie("XSRF-TOKEN");
        String jsessionId = cookieContext.getCookie("JSESSIONID");

        final Builder builder = originalRequest.headers().newBuilder();
        builder.removeAll("Cookie");
        if (jsessionId != null) {
            builder.add("Cookie", "JSESSIONID=" + jsessionId);
        }
        if (cookieXsrf != null) {
            builder.add("Cookie", "XSRF-TOKEN=" + cookieXsrf);
        }

        final Headers headers = builder.build();

        return chain.proceed(originalRequest.newBuilder()
                .method(originalRequest.method(), originalRequest.body())
                .headers(headers)
                .url(originalRequest.url())
                .build());
    }
}
