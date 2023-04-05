package niffler.ws.service;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import niffler.config.Config;
import niffler.ws.model.wsdl.AllUsersRequest;
import niffler.ws.model.wsdl.AllUsersResponse;
import niffler.ws.model.wsdl.Currency;
import niffler.ws.model.wsdl.CurrentUserRequest;
import niffler.ws.model.wsdl.CurrentUserResponse;
import niffler.ws.model.wsdl.FriendState;
import niffler.ws.model.wsdl.UpdateUserInfoRequest;
import niffler.ws.model.wsdl.UpdateUserInfoResponse;
import niffler.ws.service.converter.JaxbConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


public abstract class SoapService {

    protected static final Config CFG = Config.getConfig();

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new AllureOkHttp3())
            .build();

    private final String restServiceUrl;

    protected final Retrofit retrofit;

    protected SoapService(String restServiceUrl) {
        this.restServiceUrl = restServiceUrl;
        try {
            this.retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(this.restServiceUrl)
                    .addConverterFactory(JaxbConverterFactory.create(JAXBContext.newInstance(
                            CurrentUserRequest.class,
                            CurrentUserResponse.class,
                            UpdateUserInfoRequest.class,
                            UpdateUserInfoResponse.class,
                            AllUsersRequest.class,
                            AllUsersResponse.class,
                            Currency.class,
                            FriendState.class
                    )))
                    .build();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
