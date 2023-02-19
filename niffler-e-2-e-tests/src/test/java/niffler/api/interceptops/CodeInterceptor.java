package niffler.api.interceptops;

import niffler.api.context.SessionStorage;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

public class CodeInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        String code = chain.request().url().queryParameter("code");
        if(code != null) {
            SessionStorage.getInstance().addCode(code);
        }
        return chain.proceed(chain.request());
    }
}
