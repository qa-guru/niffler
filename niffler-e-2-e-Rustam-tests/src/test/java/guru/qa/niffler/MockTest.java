package guru.qa.niffler;

import guru.qa.niffler.jupiter.MyFullExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MyFullExtension.class)
public class MockTest extends BaseTest {

  @BeforeAll
  static void setUpAll() {
    System.out.println("    ### BeforeAll! ###");
  }

  @BeforeEach
  public void setUp() {
    System.out.println("            ### BeforeEach! ###");
  }

  @Test
  public void test() {
    System.out.println("                  ### TEST! ###");
  }

  @AfterEach
  public void tearDown() {
    System.out.println("            ### AfterEach! ###");
  }

  @AfterAll
  static void tearDownAll() {
    System.out.println("    ### AfterAll! ###");
  }

}
