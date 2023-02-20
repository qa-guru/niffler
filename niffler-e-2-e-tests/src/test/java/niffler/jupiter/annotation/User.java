package niffler.jupiter.annotation;

import niffler.jupiter.extension.CreateUserExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {

    UserType userType() default UserType.COMMON;

    CreateUserExtension.Selector selector() default CreateUserExtension.Selector.NESTED;

    @Deprecated // for study only
    enum UserType {
        ADMIN, COMMON
    }
}
