package niffler.test.niffler;

import niffler.jupiter.CallbacksExtension;
import niffler.test.BaseTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CallbacksExtension.class)
public class SecondJUnitTest extends BaseTest {

  @AfterAll
  static void afterAll() {
    System.out.println("  #### @AfterAll");
  }

  @BeforeAll
  static void beforeAll() {
    System.out.println("  #### @beforeAll");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("      #### @BeforeEach");
  }

  @AfterEach
  void afterEach() {
    System.out.println("      #### @AfterEach");
  }

  @Test
  void firstTest() {
    System.out.println("             #### @Test firstTest()");
  }

  @Test
  void secondTest() {
    System.out.println("              #### @Test secondTest()");
  }
}