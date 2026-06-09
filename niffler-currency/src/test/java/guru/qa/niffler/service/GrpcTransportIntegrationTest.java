package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.inprocess.InProcessChannelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class GrpcTransportIntegrationTest {

  private static final String SERVER_NAME = "niffler-currency-test";

  @Autowired
  private CurrencyRepository currencyRepository;

  private ManagedChannel channel;
  private NifflerCurrencyServiceBlockingStub stub;

  @BeforeEach
  void setUp() {
    channel = InProcessChannelBuilder.forName(SERVER_NAME)
        .directExecutor()
        .build();
    stub = NifflerCurrencyServiceGrpc.newBlockingStub(channel);
  }

  @AfterEach
  void tearDown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    currencyRepository.deleteAll();
  }

  @Test
  void getAllCurrenciesWhenFourCurrenciesInDbReturnsAllFour() {
    populateCurrencies();

    CurrencyResponse response = stub.getAllCurrencies(Empty.getDefaultInstance());

    Assertions.assertEquals(4, response.getAllCurrenciesCount());
  }

  @Test
  void getAllCurrenciesWhenCurrenciesInDbReturnsCorrectRates() {
    populateCurrencies();

    CurrencyResponse response = stub.getAllCurrencies(Empty.getDefaultInstance());

    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == CurrencyValues.USD && c.getCurrencyRate() == 1.0)
    );
    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == CurrencyValues.RUB && c.getCurrencyRate() == 0.015)
    );
  }

  @Test
  void getAllCurrenciesWhenDbIsEmptyReturnsEmptyList() {
    CurrencyResponse response = stub.getAllCurrencies(Empty.getDefaultInstance());

    Assertions.assertEquals(0, response.getAllCurrenciesCount());
  }

  @Test
  void calculateRateWhenRubToKztReturnsCorrectAmount() {
    populateCurrencies();

    CalculateResponse response = stub.calculateRate(CalculateRequest.newBuilder()
        .setAmount(150.0)
        .setSpendCurrency(CurrencyValues.RUB)
        .setDesiredCurrency(CurrencyValues.KZT)
        .build());

    Assertions.assertEquals(1071.43, response.getCalculatedAmount());
  }

  @Test
  void calculateRateWhenSameCurrencyReturnsSameAmount() {
    populateCurrencies();

    CalculateResponse response = stub.calculateRate(CalculateRequest.newBuilder()
        .setAmount(100.0)
        .setSpendCurrency(CurrencyValues.USD)
        .setDesiredCurrency(CurrencyValues.USD)
        .build());

    Assertions.assertEquals(100.0, response.getCalculatedAmount());
  }

  @Test
  void calculateRateWhenSpendCurrencyIsUnspecifiedReturnsInvalidArgument() {
    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> stub.calculateRate(CalculateRequest.newBuilder()
            .setAmount(100.0)
            .setSpendCurrency(CurrencyValues.UNSPECIFIED)
            .setDesiredCurrency(CurrencyValues.USD)
            .build())
    );

    Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    Assertions.assertTrue(ex.getStatus().getDescription().contains("spendCurrency"));
  }

  @Test
  void calculateRateWhenDesiredCurrencyIsUnspecifiedReturnsInvalidArgument() {
    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> stub.calculateRate(CalculateRequest.newBuilder()
            .setAmount(100.0)
            .setSpendCurrency(CurrencyValues.RUB)
            .setDesiredCurrency(CurrencyValues.UNSPECIFIED)
            .build())
    );

    Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    Assertions.assertTrue(ex.getStatus().getDescription().contains("desiredCurrency"));
  }

  @Test
  void calculateRateWhenAmountIsNegativeReturnsInvalidArgument() {
    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> stub.calculateRate(CalculateRequest.newBuilder()
            .setAmount(-1.0)
            .setSpendCurrency(CurrencyValues.RUB)
            .setDesiredCurrency(CurrencyValues.USD)
            .build())
    );

    Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
    Assertions.assertTrue(ex.getStatus().getDescription().contains("non-negative"));
  }

  @Test
  void calculateRateWhenCurrencyRateNotInDbReturnsNotFound() {
    StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> stub.calculateRate(CalculateRequest.newBuilder()
            .setAmount(100.0)
            .setSpendCurrency(CurrencyValues.RUB)
            .setDesiredCurrency(CurrencyValues.USD)
            .build())
    );

    Assertions.assertEquals(Status.Code.NOT_FOUND, ex.getStatus().getCode());
    Assertions.assertTrue(ex.getStatus().getDescription().contains("Currency rate not found"));
  }

  private void populateCurrencies() {
    CurrencyEntity rub = new CurrencyEntity();
    rub.setCurrency(guru.qa.niffler.data.CurrencyValues.RUB);
    rub.setCurrencyRate(0.015);

    CurrencyEntity usd = new CurrencyEntity();
    usd.setCurrency(guru.qa.niffler.data.CurrencyValues.USD);
    usd.setCurrencyRate(1.0);

    CurrencyEntity eur = new CurrencyEntity();
    eur.setCurrency(guru.qa.niffler.data.CurrencyValues.EUR);
    eur.setCurrencyRate(1.08);

    CurrencyEntity kzt = new CurrencyEntity();
    kzt.setCurrency(guru.qa.niffler.data.CurrencyValues.KZT);
    kzt.setCurrencyRate(0.0021);

    currencyRepository.saveAll(List.of(rub, usd, eur, kzt));
  }
}
