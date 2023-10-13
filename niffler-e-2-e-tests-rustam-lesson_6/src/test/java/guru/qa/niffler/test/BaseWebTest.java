package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.WebTest;

@WebTest
public abstract class BaseWebTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

}
