package niffler.jupiter;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@ExtendWith(UserExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {

    UserType userType() default UserType.COMMON;

    enum UserType {
        ADMIN, COMMON
    }
}
