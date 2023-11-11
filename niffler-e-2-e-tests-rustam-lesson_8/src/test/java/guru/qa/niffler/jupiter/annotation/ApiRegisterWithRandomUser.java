package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ApiRegisterExtension;
import guru.qa.niffler.jupiter.extension.ApiRegisterWithRandomUserExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(ApiRegisterWithRandomUserExtension.class)
  public @interface ApiRegisterWithRandomUser {

  String username() default "";

  String password() default "";

  String submitPassword() default "";

  GenerateRandomUser randomUser() default @GenerateRandomUser(handleAnnotation = true);
}
