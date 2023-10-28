package guru.qa.niffler.test.other;

import guru.qa.niffler.test.BaseWebTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class LessonTestRustam extends BaseWebTest {

  @BeforeEach
  void setup(String param) {
    System.out.println(param);
  }

  @Test
  @MockAnnotation
  void loginTest(){
    Allure.addAttachment("xml example", getClass().getClassLoader().getResourceAsStream(
        "rest/spend0.json"
    ));

  }

}
