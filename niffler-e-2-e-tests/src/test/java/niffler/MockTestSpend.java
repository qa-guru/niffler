package niffler;

import niffler.jupiter.MyExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MyExtension.class)
public class MockTestSpend extends BaseTestTwo {

    @Test
    void  testSpend (){
        System.out.println("Тест");
    }
}
