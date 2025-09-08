package guru.qa.niffler.service;

import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.ex.SameUsernameException;
import guru.qa.niffler.model.ErrorJson;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Value("${spring.application.name}")
  private String appName;

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorJson> handleNotFoundException(RuntimeException ex,
                                                           HttpServletRequest request) {
    LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
    return withStatus("Bad request", HttpStatus.NOT_FOUND, ex.getMessage(), request);
  }

  @ExceptionHandler(SameUsernameException.class)
  public ResponseEntity<ErrorJson> handleSameUsernameException(RuntimeException ex,
                                                               HttpServletRequest request) {
    LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
    return withStatus("Bad request", HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorJson> handleException(Exception ex,
                                                   HttpServletRequest request) {
    LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
    return withStatus("Internal error", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
  }

  private @Nonnull ResponseEntity<ErrorJson> withStatus(String type,
                                                        HttpStatus status,
                                                        String message,
                                                        HttpServletRequest request) {
    return ResponseEntity
        .status(status)
        .body(new ErrorJson(
            appName + ": " + type,
            status.getReasonPhrase(),
            status.value(),
            message,
            request.getRequestURI()
        ));
  }
}