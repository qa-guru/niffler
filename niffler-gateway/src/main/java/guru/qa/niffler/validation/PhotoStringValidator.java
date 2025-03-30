package guru.qa.niffler.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;

public class PhotoStringValidator implements ConstraintValidator<IsPhotoString, String> {

  private static final Base64.Decoder DECODER = Base64.getDecoder();

  @Override
  public boolean isValid(String string, ConstraintValidatorContext context) {
    if (!string.startsWith("data:image")) {
      return false;
    }
    String[] imageParts = string.split(",");
    if (imageParts.length != 2) {
      return false;
    }
    try {
      DECODER.decode(imageParts[1]);
      return true;
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }
}
