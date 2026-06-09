package guru.qa.niffler.api;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.api.service.RestClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class SelenoidApiClient extends RestClient {

  private final SelenoidApi selenoidApi;

  public SelenoidApiClient() {
    super(selenoidUrl(), HttpLoggingInterceptor.Level.NONE);
    this.selenoidApi = retrofit.create(SelenoidApi.class);
  }

  public byte[] download(String sessionId, String fileName) throws IOException {
    Response<ResponseBody> response = selenoidApi.download(sessionId, fileName).execute();
    ResponseBody body = response.body();
    return response.isSuccessful() && body != null
        ? body.bytes()
        : new byte[0];
  }

  @Nonnull
  private static String selenoidUrl() {
    return Configuration.remote.replaceFirst("/wd/hub/?$", "/");
  }
}
