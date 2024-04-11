package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GatewayV2ApiClient extends RestClient {

    private final GatewayV2Api gatewayV2Api;

    public GatewayV2ApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayV2Api = retrofit.create(GatewayV2Api.class);
    }

    @Step("Send REST GET('/api/v2/spends/all') request to niffler-gateway")
    @Nullable
    public RestPage<SpendJson> allSpendsPageable(@Nonnull String bearerToken,
                                                 @Nullable CurrencyValues currency,
                                                 @Nullable DataFilterValues filterPeriod,
                                                 @Nullable Integer page,
                                                 @Nullable Integer size,
                                                 @Nullable List<String> sort) throws Exception {
        return gatewayV2Api.allSpendsPageable(bearerToken, currency, filterPeriod, page, size, sort)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/v2/users/all') request to niffler-gateway")
    @Nullable
    public RestPage<UserJson> allUsersPageable(@Nonnull String bearerToken,
                                               @Nullable String searchQuery,
                                               @Nullable Integer page,
                                               @Nullable Integer size,
                                               @Nullable List<String> sort) throws Exception {
        return gatewayV2Api.allUsersPageable(bearerToken, searchQuery, page, size, sort)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/v2/friends/all') request to niffler-gateway")
    @Nullable
    public RestPage<UserJson> allFriendsPageable(@Nonnull String bearerToken,
                                                 @Nullable String searchQuery,
                                                 @Nullable Integer page,
                                                 @Nullable Integer size,
                                                 @Nullable List<String> sort) throws Exception {
        return gatewayV2Api.allFriendsPageable(bearerToken, searchQuery, page, size, sort)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/v2/invitations/income') request to niffler-gateway")
    @Nullable
    public RestPage<UserJson> incomeInvitationsPageable(@Nonnull String bearerToken,
                                                        @Nullable String searchQuery,
                                                        @Nullable Integer page,
                                                        @Nullable Integer size,
                                                        @Nullable List<String> sort) throws Exception {
        return gatewayV2Api.incomeInvitationsPageable(bearerToken, searchQuery, page, size, sort)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/v2/invitations/outcome') request to niffler-gateway")
    @Nullable
    public RestPage<UserJson> outcomeInvitationsPageable(@Nonnull String bearerToken,
                                                         @Nullable String searchQuery,
                                                         @Nullable Integer page,
                                                         @Nullable Integer size,
                                                         @Nullable List<String> sort) throws Exception {
        return gatewayV2Api.outcomeInvitationsPageable(bearerToken, searchQuery, page, size, sort)
                .execute()
                .body();
    }
}
