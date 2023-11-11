package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.Spend5Service;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.SpendJson;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;

public class Spend5RestClient extends BaseRestClient {

  public Spend5RestClient() {
    super(Config.getInstance().getSpendUrl());
  }

  private final Spend5Service spendService = retrofit.create(Spend5Service.class);

  public @Nonnull SpendJson addSpend(SpendJson spend) {
    try {
      return spendService.addSpend(spend).execute().body();
    } catch (IOException e) {
      Assertions.fail("Can`t execute api call to niffler-spend: " + e.getMessage());
      return null;
    }
  }
}
