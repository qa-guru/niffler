package guru.qa.niffler.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoWhitespaceValidatorTest {

  private final NoWhitespaceValidator validator = new NoWhitespaceValidator();
  private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

  @ValueSource(strings = {
      "foo ", "foo bar"
  })
  @ParameterizedTest
  void shouldReturnFalseForStringsWithSpaces(String input) {
    assertFalse(
        validator.isValid(input, context)
    );
  }

  @ValueSource(strings = {
      "foo", "foobar"
  })
  @ParameterizedTest
  void shouldReturnTrueForStringsWithoutSpaces(String input) {
    assertTrue(
        validator.isValid(input, context)
    );
  }
}