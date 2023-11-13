package guru.qa.niffler.service;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.ex.NoSoapResponseException;
import guru.qa.niffler.model.ErrorJson;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

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
    public ResponseEntity<ErrorJson> handleNoApiResponseException(@Nonnull RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorJson(
                        new Date(),
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        List.of(ex.getMessage())
                ));
    }
}