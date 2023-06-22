package guru.qa.niffler.api.interceptops;

import guru.qa.niffler.api.context.SessionStorageHolder;
import okhttp3.Interceptor;
import okhttp3.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ExtractCodeFromRespInterceptor implements Interceptor {

    @Override
    @Nonnull
    public Response intercept(@Nonnull Interceptor.Chain chain) throws IOException {
        String code = chain.request().url().queryParameter("code");
        if (code != null) {
            SessionStorageHolder.getInstance().addCode(code);
        }
        return chain.proceed(chain.request());
    }
}
