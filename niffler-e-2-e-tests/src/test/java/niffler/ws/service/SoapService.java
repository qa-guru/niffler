package niffler.ws.service;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import niffler.config.Config;
import niffler.userdata.wsdl.AcceptInvitationRequest;
import niffler.userdata.wsdl.AcceptInvitationResponse;
import niffler.userdata.wsdl.AddFriendRequest;
import niffler.userdata.wsdl.AddFriendResponse;
import niffler.userdata.wsdl.AllUsersRequest;
import niffler.userdata.wsdl.AllUsersResponse;
import niffler.userdata.wsdl.Currency;
import niffler.userdata.wsdl.CurrentUserRequest;
import niffler.userdata.wsdl.CurrentUserResponse;
import niffler.userdata.wsdl.DeclineInvitationRequest;
import niffler.userdata.wsdl.DeclineInvitationResponse;
import niffler.userdata.wsdl.FriendState;
import niffler.userdata.wsdl.FriendsRequest;
import niffler.userdata.wsdl.FriendsResponse;
import niffler.userdata.wsdl.InvitationsRequest;
import niffler.userdata.wsdl.InvitationsResponse;
import niffler.userdata.wsdl.RemoveFriendRequest;
import niffler.userdata.wsdl.RemoveFriendResponse;
import niffler.userdata.wsdl.UpdateUserInfoRequest;
import niffler.userdata.wsdl.UpdateUserInfoResponse;
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
