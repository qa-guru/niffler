package guru.qa.niffler.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhotoStringValidatorTest {

  private final PhotoStringValidator validator = new PhotoStringValidator();
  private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

  @NullAndEmptySource
  @ParameterizedTest
  void shouldReturnTrueForNullOrEmptyStrings(String input) {
    assertTrue(
        validator.isValid(input, context)
    );
  }

  @ValueSource(strings = {
      "plain text",
      "data:text/plain;base64,SGVsbG8=",
      "image/png;base64,iVBORw0KGgoAAAANSUhEUg=="
  })
  @ParameterizedTest
  void shouldReturnFalseIfNotStartingWithDataImage(String input) {
    assertFalse(
        validator.isValid(input, context)
    );
  }

  @ValueSource(strings = {
      "data:image/png;base64",
      "data:image/jpeg;base64"
  })
  @ParameterizedTest
  void shouldReturnFalseIfNoCommaSeparator(String input) {
    assertFalse(
        validator.isValid(input, context)
    );
  }

  @ValueSource(strings = {
      "data:image/svg;base64,SGVsbG8=",
      "data:image/tiff;base64,SGVsbG8="
  })
  @ParameterizedTest
  void shouldReturnFalseForUnsupportedMimeTypes(String input) {
    assertFalse(
        validator.isValid(input, context)
    );
  }

  @ValueSource(strings = {
      "data:image/png;base64,@@@@@@",
      "data:image/jpeg;base64,12345!@#$%"
  })
  @ParameterizedTest
  void shouldReturnFalseForInvalidBase64(String input) {
    assertFalse(
        validator.isValid(input, context)
    );
  }

  @ValueSource(strings = {
      "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg==",
      "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/"
  })
  @ParameterizedTest
  void shouldReturnTrueForValidImageData(String input) {
    assertTrue(
        validator.isValid(input, context)
    );
  }
}