package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.extension.DatabaseCreateUserExtension;
import guru.qa.niffler.jupiter.extension.GqlReqResolver;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
    DatabaseCreateUserExtension.class,
    GqlReqResolver.class,
    AllureJunit5.class})
public @interface GqlTest {

}
