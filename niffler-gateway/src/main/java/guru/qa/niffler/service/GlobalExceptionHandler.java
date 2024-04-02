package guru.qa.niffler.service;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.ex.NoSoapResponseException;
import guru.qa.niffler.model.ErrorJson;
import jakarta.annotation.Nonnull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @Nonnull ResponseEntity<Object> handleMethodArgumentNotValid(@Nonnull MethodArgumentNotValidException ex,
                                                                           @Nonnull HttpHeaders headers,
                                                                           @Nonnull HttpStatusCode status,
                                                                           @Nonnull WebRequest request) {
        return ResponseEntity
                .status(status)
                .body(new ErrorJson(
                        new Date(),
                        status.value(),
                        ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .toList()
                ));
    }

    @ExceptionHandler({NoSoapResponseException.class, NoRestResponseException.class})
    public ResponseEntity<ErrorJson> handleApiNoResponseException(@Nonnull RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorJson(
                        new Date(),
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        List.of(ex.getMessage())
                ));
    }

    @ExceptionHandler(HttpClientErrorException.NotAcceptable.class)
    public ResponseEntity<ErrorJson> handleApiNotAcceptableException(@Nonnull HttpClientErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new ErrorJson(
                        new Date(),
                        ex.getStatusCode().value(),
                        Objects.requireNonNull(ex.getResponseBodyAs(ErrorJson.class)).errors()
                ));
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ErrorJson> handleApiConflictException(@Nonnull HttpClientErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorJson(
                        new Date(),
                        ex.getStatusCode().value(),
                        Objects.requireNonNull(ex.getResponseBodyAs(ErrorJson.class)).errors()
                ));
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorJson> handleApiNotFoundException(@Nonnull HttpClientErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorJson(
                        new Date(),
                        ex.getStatusCode().value(),
                        Objects.requireNonNull(ex.getResponseBodyAs(ErrorJson.class)).errors()
                ));
    }
}