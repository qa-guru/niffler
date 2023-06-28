package guru.qa.niffler.api.service;

import guru.qa.niffler.config.Config;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RestService {

    protected static final Config CFG = Config.getConfig();
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
            .addInterceptor(new AllureOkHttp3())
            .build();

    private final String restServiceUrl;

    protected final Retrofit retrofit;

    protected RestService(String restServiceUrl) {
        this.restServiceUrl = restServiceUrl;
        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(this.restServiceUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
