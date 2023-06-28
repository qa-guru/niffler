package guru.qa.niffler.ws.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationResponse;
import guru.qa.niffler.userdata.wsdl.AddFriendRequest;
import guru.qa.niffler.userdata.wsdl.AddFriendResponse;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersResponse;
import guru.qa.niffler.userdata.wsdl.Currency;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationResponse;
import guru.qa.niffler.userdata.wsdl.FriendState;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;
import guru.qa.niffler.userdata.wsdl.InvitationsRequest;
import guru.qa.niffler.userdata.wsdl.InvitationsResponse;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendResponse;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoRequest;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoResponse;
import guru.qa.niffler.ws.service.converter.JaxbConverterFactory;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


public abstract class SoapService {

    protected static final Config CFG = Config.getConfig();

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
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
