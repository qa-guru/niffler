package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.SessionJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.StatisticJson;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi = retrofit.create(GatewayApi.class);
    }

    @Step("Send REST GET('/api/categories/all') request to niffler-gateway")
    @Nullable
    public List<CategoryJson> allCategories(String bearerToken) throws Exception {
        return gatewayApi.allCategories(bearerToken)
                .execute()
                .body();
    }

    @Step("Send REST POST('/api/categories/add') request to niffler-gateway")
    @Nullable
    public CategoryJson addCategory(String bearerToken, CategoryJson category) throws Exception {
        return gatewayApi.addCategory(bearerToken, category)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/currencies/all') request to niffler-gateway")
    @Nullable
    public List<CurrencyJson> allCurrencies(String bearerToken) throws Exception {
        return gatewayApi.allCurrencies(bearerToken)
                .execute()
                .body();
    }

    @Step("Send REST PATCH('/api/spends/edit') request to niffler-gateway")
    @Nullable
    public SpendJson editSpend(String bearerToken, SpendJson spend) throws Exception {
        return gatewayApi.editSpend(bearerToken, spend)
                .execute()
                .body();
    }

    @Step("Send REST DELETE('/api/spends/remove') request to niffler-gateway")
    public void removeSpends(String bearerToken, @Nonnull List<String> ids) throws Exception {
        gatewayApi.removeSpends(bearerToken, ids).execute();
    }

    @Step("Send REST GET('/api/friends/all') request to niffler-gateway")
    @Nullable
    public List<UserJson> allFriends(String bearerToken, @Nonnull String searchQuery) throws Exception {
        return gatewayApi.allFriends(bearerToken, searchQuery)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/stat/total') request to niffler-gateway")
    @Nullable
    public List<StatisticJson> totalStat(String bearerToken,
                                         @Nullable CurrencyValues filterCurrency,
                                         @Nullable DataFilterValues filterPeriod) throws Exception {
        return gatewayApi.totalStat(bearerToken, filterCurrency, filterPeriod)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/spends/all') request to niffler-gateway")
    @Nullable
    public List<SpendJson> allSpends(String bearerToken,
                                     @Nullable CurrencyValues filterCurrency,
                                     @Nullable DataFilterValues filterPeriod) throws Exception {
        return gatewayApi.allSpends(bearerToken, filterCurrency, filterPeriod)
                .execute()
                .body();
    }

    @Step("Send REST DELETE('/api/friends/remove') request to niffler-gateway")
    public void removeFriend(String bearerToken, @Nullable String targetUsername) throws Exception {
        gatewayApi.removeFriend(bearerToken, targetUsername)
                .execute();
    }

    @Step("Send REST GET('/api/invitations/income') request to niffler-gateway")
    @Nullable
    public List<UserJson> incomeInvitations(String bearerToken, @Nullable String searchQuery) throws Exception {
        return gatewayApi.incomeInvitations(bearerToken, searchQuery)
                .execute()
                .body();
    }

    @Step("Send REST POST('/api/spends/add') request to niffler-gateway")
    @Nullable
    public SpendJson addSpend(String bearerToken, SpendJson spend) throws Exception {
        return gatewayApi.addSpend(bearerToken, spend)
                .execute()
                .body();
    }

    @Step("Send REST POST('/api/users/update') request to niffler-gateway")
    @Nullable
    public UserJson updateUser(String bearerToken, UserJson user) throws Exception {
        return gatewayApi.updateUser(bearerToken, user)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/invitations/outcome') request to niffler-gateway")
    @Nullable
    public List<UserJson> outcomeInvitations(String bearerToken, @Nullable String searchQuery) throws Exception {
        return gatewayApi.outcomeInvitations(bearerToken, searchQuery)
                .execute()
                .body();
    }

    @Step("Send REST POST('/api/invitations/send') request to niffler-gateway")
    @Nullable
    public UserJson sendInvitation(String bearerToken, FriendJson friend) throws Exception {
        return gatewayApi.sendInvitation(bearerToken, friend)
                .execute()
                .body();
    }

    @Step("Send REST POST('/api/invitations/accept') request to niffler-gateway")
    @Nullable
    public UserJson acceptInvitation(String bearerToken, FriendJson friend) throws Exception {
        return gatewayApi.acceptInvitation(bearerToken, friend)
                .execute()
                .body();
    }

    @Step("Send REST POST('/api/invitations/decline') request to niffler-gateway")
    @Nullable
    public UserJson declineInvitation(String bearerToken, FriendJson friend) throws Exception {
        return gatewayApi.declineInvitation(bearerToken, friend)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/users/current') request to niffler-gateway")
    @Nullable
    public UserJson currentUser(String bearerToken) throws Exception {
        return gatewayApi.currentUser(bearerToken)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/users/all') request to niffler-gateway")
    @Nullable
    public List<UserJson> allUsers(String bearerToken, @Nullable String searchQuery) throws Exception {
        return gatewayApi.allUsers(bearerToken, searchQuery)
                .execute()
                .body();
    }

    @Step("Send REST GET('/api/session/current') request to niffler-gateway")
    @Nullable
    public SessionJson currentSession(String bearerToken) throws Exception {
        return gatewayApi.currentSession(bearerToken)
                .execute()
                .body();
    }
}
