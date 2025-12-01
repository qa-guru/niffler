package guru.qa.niffler.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoWhitespaceValidator implements ConstraintValidator<NoWhitespace, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !value.contains(" ");
  }
}
