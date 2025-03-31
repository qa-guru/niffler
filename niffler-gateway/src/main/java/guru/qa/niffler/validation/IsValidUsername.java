package guru.qa.niffler.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank()
@Size(min = 3, max = 50)
public @interface IsValidUsername {
  String message() default "Allowed username length should be from 3 to 50 characters";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
