package guru.qa.niffler.api;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

import guru.qa.niffler.config.Config;
import java.util.Arrays;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class BaseRestClient {

  protected static final Config CFG = Config.getConfig();

  protected final String serviceBaseUrl;
  protected final OkHttpClient httpClient;
  protected final Retrofit retrofit;

  public BaseRestClient(String serviceBaseUrl) {
    this(serviceBaseUrl, false, null);
  }

  public BaseRestClient(String serviceBaseUrl, boolean followRedirect) {
    this(serviceBaseUrl, followRedirect, null);
  }

  public BaseRestClient(String serviceBaseUrl, boolean followRedirect, Interceptor... interceptors) {
    this.serviceBaseUrl = serviceBaseUrl;
    Builder builder = new Builder()
        .followRedirects(followRedirect)
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY));

    if (interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        builder.addNetworkInterceptor(interceptor);
      }
    }

    this.httpClient = builder.build();

    this.retrofit = new Retrofit.Builder()
        .client(httpClient)
        .baseUrl(serviceBaseUrl)
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
  }
}
