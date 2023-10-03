package guru.qa.niffler.test.hw_part_1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CallbacksExtension.class)
public class FirstJUnitTest extends BaseTest {

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