package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.RestService;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.Spend7Json;

public class NifflerSpendClient extends RestService {

  public NifflerSpendClient() {
    super(CFG.getSpendUrl());
  }

  private final NifflerSpendApi nifflerSpendApi = retrofit.create(NifflerSpendApi.class);

  public Spend7Json createSpend(Spend7Json spend) throws Exception {
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
