package niffler.jupiter.annotation;

import niffler.jupiter.extension.ApiLoginExtention;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(ApiLoginExtention.class)
public @interface ApiLogin {

    String username();
    String password();
}
