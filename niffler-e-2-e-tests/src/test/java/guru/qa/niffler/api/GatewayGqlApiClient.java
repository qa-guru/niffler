package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.UpdateUserDataGql;
import guru.qa.niffler.model.gql.UserDataGql;
import guru.qa.niffler.model.gql.UsersDataGql;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

public class GatewayGqlApiClient extends RestClient {

    private final GatewayGqlApi gatewayGqlApi;

    public GatewayGqlApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayGqlApi = retrofit.create(GatewayGqlApi.class);
    }

    @Step("Send POST('/graphql') request to niffler-gateway, query: CurrentUser")
    public UserDataGql currentUser(@Nonnull String bearerToken,
                                   @Nonnull GqlRequest request) throws Exception {
        return gatewayGqlApi.currentUser(bearerToken, request)
                .execute()
                .body();
    }

    @Step("Send POST('/graphql') request to niffler-gateway, mutation: UpdateUser")
    public UpdateUserDataGql updateUser(@Nonnull String bearerToken,
                                        @Nonnull GqlRequest request) throws Exception {
        return gatewayGqlApi.updateUser(bearerToken, request)
                .execute()
                .body();
    }

    @Step("Send POST('/graphql') request to niffler-gateway, query: Users")
    public UsersDataGql allUsers(@Nonnull String bearerToken,
                                 @Nonnull GqlRequest request) throws Exception {
        return gatewayGqlApi.allUsers(bearerToken, request)
                .execute()
                .body();
    }

    @Step("Send POST('/graphql') request to niffler-gateway, query: Friends")
    public UserDataGql friends(@Nonnull String bearerToken,
                               @Nonnull GqlRequest request) throws Exception {
        return gatewayGqlApi.friends(bearerToken, request)
                .execute()
                .body();
    }
}
