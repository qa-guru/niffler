package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.TestMethodContextExtension;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class CodeInterceptor implements Interceptor {

  public static final String CODE_PARAM = "code=";

  @Override
  @Nonnull
  public Response intercept(Interceptor.Chain chain) throws IOException {
    final Response response = chain.proceed(chain.request());
    if (response.isRedirect()) {
      final String location = response.header("Location");
      if (requireNonNull(location).contains(CODE_PARAM)) {
        ApiLoginExtension.setCode(
            TestMethodContextExtension.context(),
            StringUtils.substringAfter(
                location, CODE_PARAM
            )
        );
      }
    }
    return response;
  }
}
