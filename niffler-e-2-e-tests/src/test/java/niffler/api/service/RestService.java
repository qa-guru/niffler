package niffler.api.service;

import niffler.api.NifflerSpendApi;
import niffler.config.Config;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RestService {

    protected static final Config CFG = Config.getConfig();
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private final String restServiceUrl;

    protected final Retrofit retrofit;

    protected RestService(String restServiceUrl) {
        this.restServiceUrl = restServiceUrl;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(this.restServiceUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
