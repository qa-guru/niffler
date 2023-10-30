package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.Spend7Service;
import guru.qa.niffler.model.Spend7Json;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;

public class Spend7RestClient extends BaseRestClient7 {

  public Spend7RestClient() {
    super(CFG.getSpendUrl());
  }

  private final Spend7Service spend7Service = retrofit.create(Spend7Service.class);

  public @Nonnull Spend7Json addSpend(Spend7Json spend) {
    try {
      return spend7Service.addSpend(spend).execute().body();
    } catch (IOException e) {
      Assertions.fail("Can`t execute api call to niffler-spend: " + e.getMessage());
      return null;
    }
  }
}
