package niffler.ws.service;

import niffler.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public abstract class SoapService {

    protected static final Config CFG = Config.getConfig();

    private static Strategy strategy = new AnnotationStrategy();
    private static Serializer serializer = new Persister(strategy);
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    private final String restServiceUrl;

    protected final Retrofit retrofit;

    protected SoapService(String restServiceUrl) {
        this.restServiceUrl = restServiceUrl;
        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(this.restServiceUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .build();
    }
}
