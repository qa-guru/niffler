package niffler.api;

import niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NifflerSpendClient {

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .build();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NifflerSpendService.nifflerSpendUri)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private NifflerSpendService nifflerSpendService = retrofit.create(NifflerSpendService.class);

    public SpendJson createSpend(SpendJson spend) throws Exception {
        return nifflerSpendService.addSpend(spend).execute().body();
    }

}
