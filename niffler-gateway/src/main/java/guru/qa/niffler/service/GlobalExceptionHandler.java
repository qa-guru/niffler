package guru.qa.niffler.service;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.ex.NoSoapResponseException;
import guru.qa.niffler.model.ErrorJson;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.ws.client.WebServiceIOException;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.stream.Collectors;

import static guru.qa.niffler.service.utils.ValidationExceptionMessageResolver.resolveHandlerMethodValidationException;
import static guru.qa.niffler.service.utils.ValidationExceptionMessageResolver.resolveMethodArgumentNotValidException;
import static java.util.Objects.requireNonNull;

@RestControllerAdvice
@ParametersAreNonnullByDefault
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Value("${spring.application.name}")
  private String appName;

  @Override
  protected @Nonnull ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
    return ResponseEntity
        .status(status)
        .body(new ErrorJson(
            appName + ": Entity validation error",
            HttpStatus.resolve(status.value()).getReasonPhrase(),
            status.value(),
            resolveMethodArgumentNotValidException(ex),
            ((ServletWebRequest) request).getRequest().getRequestURI()
        ));
  }

  @Override
  protected @Nonnull ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                                   HttpHeaders headers,
                                                                                   HttpStatusCode status,
                                                                                   WebRequest request) {
    return ResponseEntity
        .status(status)
        .body(new ErrorJson(
            appName + ": Entity validation error",
            requireNonNull(HttpStatus.resolve(status.value())).getReasonPhrase(),
            status.value(),
            resolveHandlerMethodValidationException(ex),
            ((ServletWebRequest) request).getRequest().getRequestURI()
        ));
  }

  @ExceptionHandler({
      HttpClientErrorException.NotAcceptable.class,
      HttpClientErrorException.Conflict.class,
      HttpClientErrorException.NotFound.class,
      HttpClientErrorException.BadRequest.class,
      HttpServerErrorException.InternalServerError.class,
      HttpServerErrorException.NotImplemented.class,
      HttpServerErrorException.ServiceUnavailable.class
  })
  public @Nonnull ResponseEntity<ErrorJson> handleRestTemplateExceptions(HttpClientErrorException ex,
                                                                         HttpServletRequest request) {
    LOG.warn("### Resolve Exception in @RestControllerAdvice ", ex);
    return handleForwardedException(ex, request);
  }

  @ExceptionHandler({
      NoSoapResponseException.class,
      NoRestResponseException.class
  })
  public @Nonnull ResponseEntity<ErrorJson> handleApiNoResponseException(RuntimeException ex,
                                                                         HttpServletRequest request) {
    LOG.warn("### Resolve Api No Response Exception in @RestControllerAdvice ", ex);
    return withStatus("Failed to collect data", HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request);
  }

  @ExceptionHandler(WebServiceIOException.class)
  public ResponseEntity<ErrorJson> handleConnectException(RuntimeException ex,
                                                          HttpServletRequest request) {
    LOG.warn("### Resolve WebServiceIOException in @RestControllerAdvice ", ex);
    return withStatus("SOAP connection refused", HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request);
  }

  @ExceptionHandler({
      ConstraintViolationException.class,
  })
  public @Nonnull ResponseEntity<ErrorJson> handleConstraintViolationException(RuntimeException ex,
                                                                               HttpServletRequest request) {
    LOG.warn("### Resolve Validation Exception in @RestControllerAdvice ", ex);
    return withStatus("Failed to collect data", HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public @Nonnull ResponseEntity<ErrorJson> handleException(Exception ex,
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

  @Nonnull
  private ResponseEntity<ErrorJson> handleForwardedException(HttpClientErrorException ex, HttpServletRequest request) {
    ErrorJson originalError = ex.getResponseBodyAs(ErrorJson.class);
    return ResponseEntity
        .status(requireNonNull(originalError).status())
        .body(new ErrorJson(
            originalError.type(),
            originalError.title(),
            originalError.status(),
            originalError.detail(),
            request.getRequestURI()
        ));
  }
}