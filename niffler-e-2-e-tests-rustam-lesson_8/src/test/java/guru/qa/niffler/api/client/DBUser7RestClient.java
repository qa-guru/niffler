package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.DBUser7Api;
import guru.qa.niffler.model.UserJson;
import java.io.IOException;
import javax.annotation.Nonnull;

public class DBUser7RestClient extends BaseRestClient7 {

  public DBUser7RestClient() {
    super(CFG.getUserdataUrl());
  }

  private final DBUser7Api dBUser7Api = retrofit.create(DBUser7Api.class);

  public @Nonnull UserJson getCurrentUser(String username) {
    try {
      return dBUser7Api.getCurrentUser(username).execute().body();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
