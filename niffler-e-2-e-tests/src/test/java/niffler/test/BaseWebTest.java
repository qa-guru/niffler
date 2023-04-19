package niffler.test;


import com.codeborne.selenide.Configuration;
import niffler.jupiter.annotation.WebTest;

@WebTest
public abstract class BaseWebTest {

  static {
    Configuration.browserSize = "1920x1080";
  }

}
