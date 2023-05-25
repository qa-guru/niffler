package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.SessionContext;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

public class RecievedCodeInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    String location = response.header("Location");
    if (location != null && location.contains("code=")) {
      SessionContext.getInstance().setCode(StringUtils.substringAfter(location, "code="));
    }
    return response;
  }
}
