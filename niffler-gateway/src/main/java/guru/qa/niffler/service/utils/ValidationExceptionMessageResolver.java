package guru.qa.niffler.service.utils;

import jakarta.annotation.Nonnull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class ValidationExceptionMessageResolver {

  @Nonnull
  public static String resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    return ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
  }

  @Nonnull
  public static String resolveHandlerMethodValidationException(HandlerMethodValidationException ex) {
    return ex.getParameterValidationResults()
        .stream()
        .flatMap(parameterValidationResult -> parameterValidationResult.getResolvableErrors().stream())
        .map(error -> {
          Object argument = Objects.requireNonNull(error.getArguments())[0];
          return ((DefaultMessageSourceResolvable) argument).getDefaultMessage() + ": " + error.getDefaultMessage();
        })
        .collect(Collectors.joining(", "));
  }
}
