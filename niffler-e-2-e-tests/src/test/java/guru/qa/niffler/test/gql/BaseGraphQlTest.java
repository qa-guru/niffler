package guru.qa.niffler.test.gql;

import com.apollographql.java.client.ApolloClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GqlTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.extension.RegisterExtension;

@GqlTest
public abstract class BaseGraphQlTest {

  protected static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
  protected static final Config CFG = Config.getConfig();

  @RegisterExtension
  protected static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  protected static final ApolloClient apolloClient = new ApolloClient.Builder()
      .serverUrl(CFG.gatewayUrl() + "graphql")
      .okHttpClient(new OkHttpClient.Builder()
          .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
          .addNetworkInterceptor(new AllureOkHttp3())
          .build())
      .build();
}
