package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ApiAuth7Extension;
import guru.qa.niffler.jupiter.extension.CreateUserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({CreateUserExtension.class, ApiAuth7Extension.class})
public @interface ApiLoginNew {

  String username() default "";

  String password() default "";

  GenerateUserNew nifflerUser() default @GenerateUserNew(handleAnnotation = false);
}
