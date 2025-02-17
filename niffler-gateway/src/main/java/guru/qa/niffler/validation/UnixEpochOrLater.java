package guru.qa.niffler.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UnixEpochOrLaterValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UnixEpochOrLater {
  String message() default "Date must be between 1970-01-01 and today";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
