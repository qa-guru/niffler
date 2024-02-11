package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CreateUserExtension;
import guru.qa.niffler.jupiter.extension.CreateUserExtension.Selector;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UserNew {

  UserType userType() default UserType.COMMON;

  Selector selector() default Selector.NESTED;

  @Deprecated // for study only
  enum UserType {
    ADMIN, COMMON
  }
}
