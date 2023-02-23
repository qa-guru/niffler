package niffler.api;

import niffler.model.UserJson;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NifflerUserdataClient {
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NifflerUserdataService.nifflerUserdataUri)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private NifflerUserdataService nifflerUserdataService = retrofit.create(NifflerUserdataService.class);

    public UserJson updateUserInfo(UserJson user) throws Exception {
        return nifflerUserdataService.updateUserInfo(user).execute().body();
    }

}
