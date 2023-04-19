package niffler.ws.service;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import niffler.config.Config;
import niffler.model.soap.AcceptInvitationRequest;
import niffler.model.soap.AcceptInvitationResponse;
import niffler.model.soap.AddFriendRequest;
import niffler.model.soap.AddFriendResponse;
import niffler.model.soap.AllUsersRequest;
import niffler.model.soap.AllUsersResponse;
import niffler.model.soap.Currency;
import niffler.model.soap.CurrentUserRequest;
import niffler.model.soap.CurrentUserResponse;
import niffler.model.soap.DeclineInvitationRequest;
import niffler.model.soap.DeclineInvitationResponse;
import niffler.model.soap.FriendState;
import niffler.model.soap.FriendsRequest;
import niffler.model.soap.FriendsResponse;
import niffler.model.soap.InvitationsRequest;
import niffler.model.soap.InvitationsResponse;
import niffler.model.soap.RemoveFriendRequest;
import niffler.model.soap.RemoveFriendResponse;
import niffler.model.soap.UpdateUserInfoRequest;
import niffler.model.soap.UpdateUserInfoResponse;
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
                            FriendsRequest.class,
                            FriendsResponse.class,
                            InvitationsRequest.class,
                            InvitationsResponse.class,
                            AcceptInvitationRequest.class,
                            AcceptInvitationResponse.class,
                            DeclineInvitationRequest.class,
                            DeclineInvitationResponse.class,
                            AddFriendRequest.class,
                            AddFriendResponse.class,
                            RemoveFriendRequest.class,
                            RemoveFriendResponse.class,
                            Currency.class,
                            FriendState.class
                    ))).build();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
