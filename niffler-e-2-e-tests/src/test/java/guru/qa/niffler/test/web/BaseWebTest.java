package guru.qa.niffler.test.web;


import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;

@WebTest
public abstract class BaseWebTest {

    protected static final Config CFG = Config.getConfig();

}
