package niffler.api;

import niffler.config.Config;
import niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NifflerSpendApi {

    Config CFG =  Config.getConfig();

    String nifflerSpendUri = CFG.spendUrl();

    @POST("/addSpend")
    Call<SpendJson> addSpend(@Body SpendJson spend);

}
