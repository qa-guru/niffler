package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CreateUserExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@ExtendWith(CreateUserExtension.class)
public @interface GenerateUserNew {

  boolean handleAnnotation() default true;

  String username() default "";

  String password() default "";

  GenerateCategoryNew[] categories() default {};

  GenerateSpendNew[] spends() default {};

}
