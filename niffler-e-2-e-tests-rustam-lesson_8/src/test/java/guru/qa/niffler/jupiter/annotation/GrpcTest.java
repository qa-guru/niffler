package guru.qa.niffler.jupiter.annotation;

import io.qameta.allure.junit5.AllureJunit5;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({AllureJunit5.class})
public @interface GrpcTest {
}
