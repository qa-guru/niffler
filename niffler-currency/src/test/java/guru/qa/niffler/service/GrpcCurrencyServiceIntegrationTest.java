package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.CurrencyResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class GrpcCurrencyServiceIntegrationTest {

  private static final String FIXTURE =
      "/guru/qa/niffler/service/GrpcCurrencyServiceIntegrationTest.sql";

  @Autowired
  private GrpcCurrencyService grpcCurrencyService;

  @Test
  @Sql(FIXTURE)
  void getAllCurrenciesShouldReturnAllFourCurrenciesFromDb() {
    CapturingStreamObserver<CurrencyResponse> observer = new CapturingStreamObserver<>();

    grpcCurrencyService.getAllCurrencies(Empty.getDefaultInstance(), observer);

    Assertions.assertTrue(observer.isCompleted());
    Assertions.assertNotNull(observer.getValue());
    Assertions.assertEquals(4, observer.getValue().getAllCurrenciesCount());
  }

  @Test
  @Sql(FIXTURE)
  void getAllCurrenciesShouldContainCorrectRatesFromDb() {
    CapturingStreamObserver<CurrencyResponse> observer = new CapturingStreamObserver<>();

    grpcCurrencyService.getAllCurrencies(Empty.getDefaultInstance(), observer);

    CurrencyResponse response = observer.getValue();
    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == guru.qa.niffler.grpc.CurrencyValues.USD
                       && c.getCurrencyRate() == 1.0)
    );
    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == guru.qa.niffler.grpc.CurrencyValues.RUB
                       && c.getCurrencyRate() == 0.015)
    );
    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == guru.qa.niffler.grpc.CurrencyValues.EUR
                       && c.getCurrencyRate() == 1.08)
    );
    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == guru.qa.niffler.grpc.CurrencyValues.KZT
                       && c.getCurrencyRate() == 0.0021)
    );
  }

  @Test
  @Sql(FIXTURE)
  void calculateRateShouldConvertRubToKztUsingDbRates() {
    CapturingStreamObserver<CalculateResponse> observer = new CapturingStreamObserver<>();
    CalculateRequest request = CalculateRequest.newBuilder()
        .setAmount(150.0)
        .setSpendCurrency(guru.qa.niffler.grpc.CurrencyValues.RUB)
        .setDesiredCurrency(guru.qa.niffler.grpc.CurrencyValues.KZT)
        .build();

    grpcCurrencyService.calculateRate(request, observer);

    Assertions.assertTrue(observer.isCompleted());
    Assertions.assertEquals(1071.43, observer.getValue().getCalculatedAmount());
  }

  @Test
  @Sql(FIXTURE)
  void calculateRateShouldConvertUsdToEurUsingDbRates() {
    CapturingStreamObserver<CalculateResponse> observer = new CapturingStreamObserver<>();
    CalculateRequest request = CalculateRequest.newBuilder()
        .setAmount(34.0)
        .setSpendCurrency(guru.qa.niffler.grpc.CurrencyValues.USD)
        .setDesiredCurrency(guru.qa.niffler.grpc.CurrencyValues.EUR)
        .build();

    grpcCurrencyService.calculateRate(request, observer);

    Assertions.assertTrue(observer.isCompleted());
    Assertions.assertEquals(31.48, observer.getValue().getCalculatedAmount());
  }

  @Test
  @Sql(FIXTURE)
  void calculateRateShouldReturnSameAmountForSameCurrency() {
    CapturingStreamObserver<CalculateResponse> observer = new CapturingStreamObserver<>();
    CalculateRequest request = CalculateRequest.newBuilder()
        .setAmount(150.0)
        .setSpendCurrency(guru.qa.niffler.grpc.CurrencyValues.RUB)
        .setDesiredCurrency(guru.qa.niffler.grpc.CurrencyValues.RUB)
        .build();

    grpcCurrencyService.calculateRate(request, observer);

    Assertions.assertTrue(observer.isCompleted());
    Assertions.assertEquals(150.0, observer.getValue().getCalculatedAmount());
  }

  private static class CapturingStreamObserver<T> implements StreamObserver<T> {

    private T value;
    private final AtomicBoolean completed = new AtomicBoolean(false);
    private final List<Throwable> errors = new ArrayList<>();

    @Override
    public void onNext(T value) {
      this.value = value;
    }

    @Override
    public void onError(Throwable t) {
      errors.add(t);
    }

    @Override
    public void onCompleted() {
      completed.set(true);
    }

    T getValue() {
      return value;
    }

    boolean isCompleted() {
      return completed.get();
    }
  }
}
