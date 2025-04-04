package guru.qa.niffler.api.service;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.CookieManager;
import java.net.CookiePolicy;

@ParametersAreNonnullByDefault
public abstract class RestClient {

  protected static final Config CFG = Config.getInstance();

  protected final OkHttpClient httpClient;
  protected final Retrofit retrofit;

  public RestClient(String baseUrl) {
    this(baseUrl, false, JacksonConverterFactory.create(), CFG.restLoggingLevel());
  }

  public RestClient(String baseUrl,
                    HttpLoggingInterceptor.Level httpLogLevel) {
    this(baseUrl, false, JacksonConverterFactory.create(), httpLogLevel);
  }

  public RestClient(String baseUrl,
                    boolean followRedirect) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), CFG.restLoggingLevel());
  }

  public RestClient(String baseUrl,
                    boolean followRedirect,
                    HttpLoggingInterceptor.Level httpLogLevel) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), httpLogLevel);
  }

  public RestClient(String baseUrl,
                    boolean followRedirect,
                    Interceptor... interceptors) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), CFG.restLoggingLevel(), interceptors);
  }

  public RestClient(String baseUrl,
                    boolean followRedirect,
                    HttpLoggingInterceptor.Level httpLogLevel,
                    Interceptor... interceptors) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), httpLogLevel, interceptors);
  }

  public RestClient(String baseUrl,
                    boolean followRedirect,
                    Converter.Factory converterFactory,
                    HttpLoggingInterceptor.Level httpLogLevel,
                    @Nullable Interceptor... interceptors) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .followRedirects(followRedirect);

    if (interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        builder.addNetworkInterceptor(interceptor);
      }
    }

    builder.cookieJar(
        new JavaNetCookieJar(
            new CookieManager(
                ThreadLocalCookieStore.INSTANCE,
                CookiePolicy.ACCEPT_ALL
            )
        )
    );
    builder.addNetworkInterceptor(
        new HttpLoggingInterceptor().setLevel(httpLogLevel)
    );

    this.httpClient = builder.build();
    this.retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(converterFactory)
        .build();
  }
}
