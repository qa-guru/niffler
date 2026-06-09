package guru.qa.niffler.service.api;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.model.CurrencyJson;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.grpc.client.ChannelBuilderOptions;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

class GrpcCurrencyClientTransportTest {

  private Server server;
  private ManagedChannel channel;
  private GrpcCurrencyClient client;

  private NifflerCurrencyServiceGrpc.NifflerCurrencyServiceImplBase serviceImpl;

  @BeforeEach
  void setUp() throws IOException {
    String serverName = InProcessServerBuilder.generateName();
    server = InProcessServerBuilder.forName(serverName)
        .directExecutor()
        .addService(new DelegatingCurrencyService())
        .build()
        .start();
    channel = InProcessChannelBuilder.forName(serverName)
        .directExecutor()
        .build();
    GrpcChannelFactory channelFactory = new GrpcChannelFactory() {
      @Override
      public boolean supports(String target) {
        return true;
      }

      @Override
      public ManagedChannel createChannel(String target, ChannelBuilderOptions options) {
        return channel;
      }
    };
    client = new GrpcCurrencyClient(channelFactory);
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  @Test
  void getAllCurrenciesWhenServerRespondsSuccessfullyReturnsMappedList() {
    serviceImpl = new SuccessNifflerCurrencyService();

    List<CurrencyJson> result = client.getAllCurrencies();

    Assertions.assertEquals(2, result.size());
    Assertions.assertTrue(result.stream().anyMatch(c -> c.currency() == guru.qa.niffler.model.CurrencyValues.USD));
    Assertions.assertTrue(result.stream().anyMatch(c -> c.currency() == guru.qa.niffler.model.CurrencyValues.RUB));
  }

  @Test
  void getAllCurrenciesWhenServerReturnsNotFoundPropagatesNotFoundStatus() {
    serviceImpl = new ErrorNifflerCurrencyService(Status.NOT_FOUND.withDescription("Currency rate not found"));

    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> client.getAllCurrencies()
    );

    Assertions.assertEquals(Status.Code.NOT_FOUND, ex.getStatus().getCode());
    Assertions.assertTrue(ex.getStatus().getDescription().contains("Currency rate not found"));
  }

  @Test
  void getAllCurrenciesWhenServerReturnsInvalidArgumentPropagatesStatus() {
    serviceImpl = new ErrorNifflerCurrencyService(Status.INVALID_ARGUMENT.withDescription("bad request"));

    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> client.getAllCurrencies()
    );

    Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
  }

  @Test
  void getAllCurrenciesWhenServerIsUnavailablePropagatesStatus() {
    serviceImpl = new ErrorNifflerCurrencyService(Status.UNAVAILABLE.withDescription("server down"));

    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> client.getAllCurrencies()
    );

    Assertions.assertEquals(Status.Code.UNAVAILABLE, ex.getStatus().getCode());
  }

  @Test
  void getAllCurrenciesWhenServerReturnsInternalPropagatesStatus() {
    serviceImpl = new ErrorNifflerCurrencyService(Status.INTERNAL.withDescription("unexpected error"));

    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> client.getAllCurrencies()
    );

    Assertions.assertEquals(Status.Code.INTERNAL, ex.getStatus().getCode());
  }

  @Test
  void getAllCurrenciesWhenServerReturnsPermissionDeniedPropagatesStatus() {
    serviceImpl = new ErrorNifflerCurrencyService(Status.PERMISSION_DENIED.withDescription("access denied"));

    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> client.getAllCurrencies()
    );

    Assertions.assertEquals(Status.Code.PERMISSION_DENIED, ex.getStatus().getCode());
  }

  private class DelegatingCurrencyService extends NifflerCurrencyServiceGrpc.NifflerCurrencyServiceImplBase {

    @Override
    public void getAllCurrencies(Empty request, StreamObserver<CurrencyResponse> responseObserver) {
      if (serviceImpl != null) {
        serviceImpl.getAllCurrencies(request, responseObserver);
      } else {
        responseObserver.onNext(CurrencyResponse.getDefaultInstance());
        responseObserver.onCompleted();
      }
    }
  }

  private static class SuccessNifflerCurrencyService extends NifflerCurrencyServiceGrpc.NifflerCurrencyServiceImplBase {

    @Override
    public void getAllCurrencies(Empty request, StreamObserver<CurrencyResponse> responseObserver) {
      responseObserver.onNext(CurrencyResponse.newBuilder()
          .addAllAllCurrencies(List.of(
              Currency.newBuilder().setCurrency(CurrencyValues.USD).setCurrencyRate(1.0).build(),
              Currency.newBuilder().setCurrency(CurrencyValues.RUB).setCurrencyRate(0.015).build()
          ))
          .build());
      responseObserver.onCompleted();
    }
  }

  private static class ErrorNifflerCurrencyService extends NifflerCurrencyServiceGrpc.NifflerCurrencyServiceImplBase {

    private final Status errorStatus;

    ErrorNifflerCurrencyService(Status errorStatus) {
      this.errorStatus = errorStatus;
    }

    @Override
    public void getAllCurrencies(Empty request, StreamObserver<CurrencyResponse> responseObserver) {
      responseObserver.onError(errorStatus.asRuntimeException());
    }
  }
}
