package guru.qa.niffler.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SelenoidApi {

  @GET("download/{sessionId}/{fileName}")
  Call<ResponseBody> download(
      @Path("sessionId") String sessionId,
      @Path("fileName") String fileName
  );
}
