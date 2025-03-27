package guru.qa.niffler.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Date;

public class UnixEpochOrLaterValidator implements ConstraintValidator<UnixEpochOrLater, Date> {
  private static final long UNIX_EPOCH_TIME = 0L;

  @Override
  public boolean isValid(Date date, ConstraintValidatorContext context) {
    long time = date.getTime();
    long now = System.currentTimeMillis();
    return time >= UNIX_EPOCH_TIME && time <= now;
  }
}
