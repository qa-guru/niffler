package niffler.jupiter.annotation;


import niffler.jupiter.extension.ClearCookiesAndSessionExtension;
import niffler.jupiter.extension.CreateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@ExtendWith({CreateUserExtension.class, ClearCookiesAndSessionExtension.class})
public @interface GenerateUser {

    boolean handleAnnotation() default true;

    String username() default "";

    String password() default "";

    GenerateCategory[] categories() default {};

    GenerateSpend[] spends() default {};

    Friends friends() default @Friends(handleAnnotation = false);

    Invitations invitations() default @Invitations(handleAnnotation = false);
}
