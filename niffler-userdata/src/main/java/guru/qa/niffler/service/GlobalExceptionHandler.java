package guru.qa.niffler.service;

import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.ex.SameUsernameException;
import guru.qa.niffler.model.ErrorJson;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.name}")
    private String appName;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorJson> handleNotFoundException(@Nonnull RuntimeException ex,
                                                             @Nonnull HttpServletRequest request) {
        return withStatus("Bad request", HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(SameUsernameException.class)
    public ResponseEntity<ErrorJson> handleSameUsernameException(@Nonnull RuntimeException ex,
                                                                 @Nonnull HttpServletRequest request) {
        return withStatus("Bad request", HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorJson> handleException(@Nonnull Exception ex,
                                                     @Nonnull HttpServletRequest request) {
        return withStatus("Internal error", HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    private @Nonnull ResponseEntity<ErrorJson> withStatus(@Nonnull String type,
                                                          @Nonnull HttpStatus status,
                                                          @Nonnull Exception ex,
                                                          @Nonnull HttpServletRequest request) {
        return ResponseEntity
                .status(status)
                .body(new ErrorJson(
                        appName + ": " + type,
                        status.getReasonPhrase(),
                        status.value(),
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }
}