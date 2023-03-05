package niffler.api;

import niffler.api.service.RestService;
import niffler.model.CategoryJson;
import niffler.model.SpendJson;

public class NifflerSpendClient extends RestService {

    public NifflerSpendClient() {
        super(CFG.spendUrl());
    }

    private final NifflerSpendApi nifflerSpendApi = retrofit.create(NifflerSpendApi.class);

    public SpendJson createSpend(SpendJson spend) throws Exception {
        return nifflerSpendApi.addSpend(spend)
                .execute()
                .body();
    }

    public CategoryJson createCategory(CategoryJson category) throws Exception {
        return nifflerSpendApi.addCategory(category)
                .execute()
                .body();
    }
}
