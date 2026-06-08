package guru.qa.niffler.service;

import io.grpc.Status;
import io.grpc.StatusException;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class GrpcExceptionAdvice implements GrpcExceptionHandler {

  @Override
  public StatusException handleException(Throwable exception) {
    return switch (exception) {
      case IllegalArgumentException ex -> Status.INVALID_ARGUMENT
          .withDescription(ex.getMessage())
          .asException();
      case NoSuchElementException ignored -> Status.NOT_FOUND
          .withDescription("Currency rate not found")
          .asException();
      default -> null;
    };
  }
}
