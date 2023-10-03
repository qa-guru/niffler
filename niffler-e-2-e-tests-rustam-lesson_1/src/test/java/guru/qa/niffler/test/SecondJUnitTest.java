package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.CallbacksExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CallbacksExtension.class)
public class SecondJUnitTest extends BaseTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("    #### @beforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("    #### @AfterAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("            #### @BeforeEach");
    }

    @AfterEach
    void afterEach() {
        System.out.println("            #### @AfterEach");
    }

    @Test
    void firstTest() {
        System.out.println("                       #### @Test firstTest()");
    }

    @Test
    void secondTest() {
        System.out.println("                       #### @Test secondTest()");
//        throw new IllegalStateException();
    }
}