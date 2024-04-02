package guru.qa.niffler.service;

import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.NotUniqCategoryException;
import guru.qa.niffler.ex.SpendNotFoundException;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.ErrorJson;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TooManyCategoriesException.class)
    public ResponseEntity<ErrorJson> handleNotAcceptableException(@Nonnull RuntimeException ex) {
        return withStatus(HttpStatus.NOT_ACCEPTABLE, ex);
    }

    @ExceptionHandler(NotUniqCategoryException.class)
    public ResponseEntity<ErrorJson> handleNotUniqCategoryException(@Nonnull RuntimeException ex) {
        return withStatus(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler({CategoryNotFoundException.class, SpendNotFoundException.class})
    public ResponseEntity<ErrorJson> handleNotFoundException(@Nonnull RuntimeException ex) {
        return withStatus(HttpStatus.NOT_FOUND, ex);
    }

    private @Nonnull ResponseEntity<ErrorJson> withStatus(@Nonnull HttpStatus status, @Nonnull RuntimeException ex) {
        return ResponseEntity
                .status(status)
                .body(new ErrorJson(
                        new Date(),
                        status.value(),
                        List.of(ex.getMessage())
                ));
    }
}