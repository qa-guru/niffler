package guru.qa.niffler.page;

import guru.qa.niffler.config.Config;
public abstract class BasePageRustam<T extends BasePageRustam> {

  protected static final Config CFG = Config.getInstance();
  public abstract T checkThatPageLoaded();
}
