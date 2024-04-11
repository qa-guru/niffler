package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.ContextHolderExtension;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.io.IOException;

public class CodeInterceptor implements Interceptor {

    public static final String CODE_PARAM = "code=";

    @Override
    @Nonnull
    public Response intercept(@Nonnull Interceptor.Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        if (response.isRedirect()) {
            final String location = response.header("Location");
            if (location.contains(CODE_PARAM)) {
                ApiLoginExtension.setCode(
                        ContextHolderExtension.context(),
                        StringUtils.substringAfter(
                                location, CODE_PARAM
                        )
                );
            }
        }
        return response;
    }
}
