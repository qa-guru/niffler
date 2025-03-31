package guru.qa.niffler.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Base64;
import java.util.Set;

public class PhotoStringValidator implements ConstraintValidator<IsPhotoString, String> {

  private static final Base64.Decoder DECODER = Base64.getDecoder();
  private static final Set<String> ALLOWED_FORMATS = Set.of("jpeg", "jpg", "png", "bmp", "wbmp", "gif");

  @Override
  public boolean isValid(String string, ConstraintValidatorContext context) {
    if (string == null) {
      return true;
    }
    if (!string.startsWith("data:image/")) {
      return false;
    }

    final String[] imageParts = string.split(",", 2);
    if (imageParts.length != 2) {
      return false;
    }

    final String mimeType = imageParts[0].substring(11).split(";")[0].toLowerCase();
    if (!ALLOWED_FORMATS.contains(mimeType)) {
      return false;
    }

    try {
      DECODER.decode(imageParts[1]);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
