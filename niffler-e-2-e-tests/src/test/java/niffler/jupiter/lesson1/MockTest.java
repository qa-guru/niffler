package niffler.jupiter.lesson1;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@ExtendWith(MyFullExtension.class)
public class MockTest extends BaseTest {

    @BeforeAll
    static void setUpAll() {
        System.out.println("   ### BeforeAll! ###");
    }

    @BeforeEach
    void setUp() {
        System.out.println("            ### BeforeEach! ###");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("   ### AfterAll! ###");
    }

    @AfterEach
    void tearDown() {
        System.out.println("            ### AfterEach! ###");
    }

    @Test
    public void test() {


        System.out.println("                        ### TEST! ###");
    }

}
