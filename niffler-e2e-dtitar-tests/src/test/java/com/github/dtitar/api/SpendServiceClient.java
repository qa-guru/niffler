package com.github.dtitar.api;

import com.github.dtitar.model.rest.CategoryJson;
import com.github.dtitar.model.rest.SpendJson;
import io.qameta.allure.Step;

import java.io.IOException;
import java.util.List;

public class SpendServiceClient extends RestService {

    public SpendServiceClient() {
        super(CFG.spendUrl());
    }

    private final SpendService spendService = retrofit.create(SpendService.class);

    @Step("Create spend")
    public SpendJson addSpend(SpendJson spend) throws IOException {
        return spendService.addSpend(spend)
                .execute()
                .body();
    }

    @Step("Create category")
    public void addCategory(CategoryJson category) throws IOException {
        spendService.addCategory(category)
                .execute();
    }

    @Step("Get all spends")
    public List<SpendJson> getSpends(String username) throws IOException {
        return spendService.getSpends(username)
                .execute()
                .body();
    }

    @Step("Delete all spends")
    public void deleteSpends(String username, List<String> ids) throws IOException {
        spendService.deleteSpends(username, ids)
                .execute();
    }
}
