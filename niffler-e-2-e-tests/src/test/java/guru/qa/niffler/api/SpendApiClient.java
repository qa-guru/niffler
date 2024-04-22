package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestClient;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.StatisticJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Step("Send REST POST('/internal/spends/add') request to niffler-spend")
    @Nullable
    public SpendJson createSpend(@Nonnull SpendJson spend) throws Exception {
        return spendApi.addSpend(spend)
                .execute()
                .body();
    }

    @Step("Send REST POST('/internal/categories/add') request to niffler-spend")
    @Nullable
    public CategoryJson createCategory(@Nonnull CategoryJson category) throws Exception {
        return spendApi.addCategory(category)
                .execute()
                .body();
    }

    @Step("Send REST GET('/internal/v2/spends/all') request to niffler-spend")
    @Nullable
    public RestPage<SpendJson> allSpendsPageable(@Nonnull String username,
                                                 @Nullable CurrencyValues currency,
                                                 @Nullable String from,
                                                 @Nullable String to,
                                                 @Nullable Integer page,
                                                 @Nullable Integer size,
                                                 @Nullable List<String> sort) throws Exception {
        return spendApi.allSpendsPageable(username, currency, from, to, page, size, sort)
                .execute()
                .body();
    }

    @Step("Send REST GET('/internal/stat/total') request to niffler-spend")
    @Nullable
    public List<StatisticJson> statistic(@Nonnull String username,
                                         @Nullable CurrencyValues userCurrency,
                                         @Nullable CurrencyValues filterCurrency,
                                         @Nullable String from,
                                         @Nullable String to) throws Exception {
        return spendApi.stat(username, userCurrency, filterCurrency, from, to)
                .execute()
                .body();
    }
}
