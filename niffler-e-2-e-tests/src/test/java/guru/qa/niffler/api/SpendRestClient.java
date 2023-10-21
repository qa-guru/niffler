package guru.qa.niffler.api;

import guru.qa.niffler.api.service.RestService;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpendRestClient extends RestService {

    public SpendRestClient() {
        super(CFG.spendUrl());
    }

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Step("Send REST POST('/addSpend') request to niffler-spend")
    @Nullable
    public SpendJson createSpend(@Nonnull SpendJson spend) throws Exception {
        return spendApi.addSpend(spend)
                .execute()
                .body();
    }

    @Step("Send REST POST('/category') request to niffler-spend")
    @Nullable
    public CategoryJson createCategory(@Nonnull CategoryJson category) throws Exception {
        return spendApi.addCategory(category)
                .execute()
                .body();
    }
}
