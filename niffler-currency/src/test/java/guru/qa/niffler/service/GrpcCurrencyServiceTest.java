package guru.qa.niffler.service;

import com.google.protobuf.Empty;
import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.CurrencyResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static guru.qa.niffler.data.CurrencyValues.EUR;
import static guru.qa.niffler.data.CurrencyValues.KZT;
import static guru.qa.niffler.data.CurrencyValues.RUB;
import static guru.qa.niffler.data.CurrencyValues.USD;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GrpcCurrencyServiceTest {

  private GrpcCurrencyService grpcCurrencyService;
  private List<CurrencyEntity> testCurrencies;

  @BeforeEach
  void setUp(@Mock CurrencyRepository currencyRepository) {
    CurrencyEntity rub = new CurrencyEntity();
    rub.setCurrency(RUB);
    rub.setCurrencyRate(0.015);
    CurrencyEntity usd = new CurrencyEntity();
    usd.setCurrency(USD);
    usd.setCurrencyRate(1.0);
    CurrencyEntity eur = new CurrencyEntity();
    eur.setCurrency(EUR);
    eur.setCurrencyRate(1.08);
    CurrencyEntity kzt = new CurrencyEntity();
    kzt.setCurrency(KZT);
    kzt.setCurrencyRate(0.0021);

    testCurrencies = List.of(rub, kzt, eur, usd);

    lenient().when(currencyRepository.findAll())
        .thenReturn(testCurrencies);

    grpcCurrencyService = new GrpcCurrencyService(currencyRepository);
  }

  static Stream<Arguments> spendCurrencyShouldBeConverted() {
    return Stream.of(
        Arguments.of(150.00, guru.qa.niffler.grpc.CurrencyValues.RUB, guru.qa.niffler.grpc.CurrencyValues.KZT, 1071.43),
        Arguments.of(34.00, guru.qa.niffler.grpc.CurrencyValues.USD, guru.qa.niffler.grpc.CurrencyValues.EUR, 31.48),
        Arguments.of(150.00, guru.qa.niffler.grpc.CurrencyValues.RUB, guru.qa.niffler.grpc.CurrencyValues.RUB, 150.00),
        Arguments.of(0.00, guru.qa.niffler.grpc.CurrencyValues.KZT, guru.qa.niffler.grpc.CurrencyValues.RUB, 0.00)
    );
  }

  @MethodSource
  @ParameterizedTest
  void spendCurrencyShouldBeConverted(double spend,
                                      guru.qa.niffler.grpc.CurrencyValues spendCurrency,
                                      guru.qa.niffler.grpc.CurrencyValues desiredCurrency,
                                      double expectedResult) {

    BigDecimal result = grpcCurrencyService.convertSpendTo(spend, spendCurrency,
        desiredCurrency, testCurrencies);

    Assertions.assertEquals(expectedResult, result.doubleValue());
  }

  static Stream<Arguments> additionalConversionCases() {
    return Stream.of(
        Arguments.of(100.0, guru.qa.niffler.grpc.CurrencyValues.USD, guru.qa.niffler.grpc.CurrencyValues.RUB, 6666.67),
        Arguments.of(100.0, guru.qa.niffler.grpc.CurrencyValues.USD, guru.qa.niffler.grpc.CurrencyValues.USD, 100.0),
        Arguments.of(100.0, guru.qa.niffler.grpc.CurrencyValues.EUR, guru.qa.niffler.grpc.CurrencyValues.KZT, 51428.57),
        Arguments.of(1000.0, guru.qa.niffler.grpc.CurrencyValues.KZT, guru.qa.niffler.grpc.CurrencyValues.EUR, 1.94)
    );
  }

  @MethodSource
  @ParameterizedTest
  void additionalConversionCases(double spend,
                                  guru.qa.niffler.grpc.CurrencyValues spendCurrency,
                                  guru.qa.niffler.grpc.CurrencyValues desiredCurrency,
                                  double expectedResult) {
    BigDecimal result = grpcCurrencyService.convertSpendTo(spend, spendCurrency,
        desiredCurrency, testCurrencies);

    Assertions.assertEquals(expectedResult, result.doubleValue());
  }

  @Test
  void convertSpendShouldThrowWhenCurrencyNotFound() {
    Assertions.assertThrows(
        NoSuchElementException.class,
        () -> grpcCurrencyService.convertSpendTo(
            100.0,
            guru.qa.niffler.grpc.CurrencyValues.RUB,
            guru.qa.niffler.grpc.CurrencyValues.RUB,
            List.of()
        )
    );
  }

  @Test
  void getAllCurrenciesShouldReturnAllFourCurrencies(@Mock CurrencyRepository currencyRepository) {
    lenient().when(currencyRepository.findAll()).thenReturn(testCurrencies);
    GrpcCurrencyService service = new GrpcCurrencyService(currencyRepository);

    @SuppressWarnings("unchecked")
    StreamObserver<CurrencyResponse> observer = mock(StreamObserver.class);
    service.getAllCurrencies(Empty.getDefaultInstance(), observer);

    ArgumentCaptor<CurrencyResponse> captor = ArgumentCaptor.forClass(CurrencyResponse.class);
    verify(observer).onNext(captor.capture());
    verify(observer).onCompleted();

    CurrencyResponse response = captor.getValue();
    Assertions.assertEquals(4, response.getAllCurrenciesCount());
    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == guru.qa.niffler.grpc.CurrencyValues.RUB
                       && c.getCurrencyRate() == 0.015)
    );
    Assertions.assertTrue(
        response.getAllCurrenciesList().stream()
            .anyMatch(c -> c.getCurrency() == guru.qa.niffler.grpc.CurrencyValues.USD
                       && c.getCurrencyRate() == 1.0)
    );
  }

  @Test
  void calculateRateShouldReturnCorrectCalculatedAmount(@Mock CurrencyRepository currencyRepository) {
    lenient().when(currencyRepository.findAll()).thenReturn(testCurrencies);
    GrpcCurrencyService service = new GrpcCurrencyService(currencyRepository);

    CalculateRequest request = CalculateRequest.newBuilder()
        .setAmount(150.0)
        .setSpendCurrency(guru.qa.niffler.grpc.CurrencyValues.RUB)
        .setDesiredCurrency(guru.qa.niffler.grpc.CurrencyValues.KZT)
        .build();

    @SuppressWarnings("unchecked")
    StreamObserver<CalculateResponse> observer = mock(StreamObserver.class);
    service.calculateRate(request, observer);

    ArgumentCaptor<CalculateResponse> captor = ArgumentCaptor.forClass(CalculateResponse.class);
    verify(observer).onNext(captor.capture());
    verify(observer).onCompleted();

    Assertions.assertEquals(1071.43, captor.getValue().getCalculatedAmount());
  }

  @Test
  void calculateRateShouldCallOnCompletedAfterOnNext(@Mock CurrencyRepository currencyRepository) {
    lenient().when(currencyRepository.findAll()).thenReturn(testCurrencies);
    GrpcCurrencyService service = new GrpcCurrencyService(currencyRepository);

    CalculateRequest request = CalculateRequest.newBuilder()
        .setAmount(34.0)
        .setSpendCurrency(guru.qa.niffler.grpc.CurrencyValues.USD)
        .setDesiredCurrency(guru.qa.niffler.grpc.CurrencyValues.EUR)
        .build();

    @SuppressWarnings("unchecked")
    StreamObserver<CalculateResponse> observer = mock(StreamObserver.class);
    service.calculateRate(request, observer);

    org.mockito.InOrder inOrder = org.mockito.Mockito.inOrder(observer);
    inOrder.verify(observer).onNext(org.mockito.ArgumentMatchers.any());
    inOrder.verify(observer).onCompleted();
  }
}