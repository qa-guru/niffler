package niffler.api;

import niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class NifflerSpendClient {

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NifflerSpendService.nifflerSpendUri)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final NifflerSpendService nifflerSpendService = retrofit.create(NifflerSpendService.class);

    public SpendJson createSpend(SpendJson spend) throws Exception {
        return nifflerSpendService.addSpend(spend).execute().body();
    }

    public void deleteSpend(String username, List<String> ids) throws Exception {
        nifflerSpendService.deleteSpend(username, ids).execute();
    }

}
