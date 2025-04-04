package guru.qa.niffler.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnixEpochOrLaterValidatorTest {

  private static final long UNIX_EPOCH_TIME = 0L; // 1970-01-01 00:00:00 UTC

  private final UnixEpochOrLaterValidator validator = new UnixEpochOrLaterValidator();
  private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

  @Test
  void shouldReturnFalseForNullDate() {
    assertFalse(
        validator.isValid(null, context)
    );
  }

  @Test
  void shouldReturnFalseForDateBeforeUnixEpoch() {
    assertFalse(
        validator.isValid(new Date(UNIX_EPOCH_TIME - 1000), context)
    );
  }

  @Test
  void shouldReturnFalseForFutureDate() {
    assertFalse(
        validator.isValid(new Date(System.currentTimeMillis() + 1000), context)
    );
  }

  @Test
  void shouldReturnTrueForValidDateWithinRange() {
    assertTrue(
        validator.isValid(new Date(System.currentTimeMillis() - 10000), context)
    );
  }

  @Test
  void shouldReturnTrueForUnixEpochTime() {
    assertTrue(
        validator.isValid(new Date(UNIX_EPOCH_TIME), context)
    );
  }
}