package guru.qa.niffler.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhotoStringValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsPhotoString {
  String message() default "Photo must be a base64 string in allowed formats: 'jpeg', 'jpg', 'png', 'bmp', 'wbmp', 'gif'";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
