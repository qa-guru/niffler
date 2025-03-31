package guru.qa.niffler.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UnixEpochOrLaterValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnixEpochOrLater {
  String message() default "The string is not a base64 image string";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
