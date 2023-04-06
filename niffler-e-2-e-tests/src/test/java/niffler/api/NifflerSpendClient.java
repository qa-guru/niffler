package niffler.api;

import io.qameta.allure.Step;
import niffler.api.service.RestService;
import niffler.model.rest.CategoryJson;
import niffler.model.rest.SpendJson;

public class NifflerSpendClient extends RestService {

    public NifflerSpendClient() {
        super(CFG.spendUrl());
    }

    private final NifflerSpendApi nifflerSpendApi = retrofit.create(NifflerSpendApi.class);

    @Step("Send REST POST('/addSpend') request to niffler-spend")
    public SpendJson createSpend(SpendJson spend) throws Exception {
        return nifflerSpendApi.addSpend(spend)
                .execute()
                .body();
    }

    @Step("Send REST POST('/category') request to niffler-spend")
    public CategoryJson createCategory(CategoryJson category) throws Exception {
        return nifflerSpendApi.addCategory(category)
                .execute()
                .body();
    }
}
