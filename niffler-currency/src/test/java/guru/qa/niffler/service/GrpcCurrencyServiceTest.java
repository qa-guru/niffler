package guru.qa.niffler.service;

import static guru.qa.niffler.data.CurrencyValues.EUR;
import static guru.qa.niffler.data.CurrencyValues.KZT;
import static guru.qa.niffler.data.CurrencyValues.RUB;
import static guru.qa.niffler.data.CurrencyValues.USD;
import static org.mockito.Mockito.lenient;

import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GrpcCurrencyServiceTest {

  GrpcCurrencyService grpcCurrencyService;
  List<CurrencyEntity> testCurrencies;

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

    lenient()
        .when(currencyRepository.findAll())
        .thenReturn(testCurrencies);

    grpcCurrencyService = new GrpcCurrencyService(currencyRepository);
  }


  static Stream<Arguments> convertSpendTo() {
    return Stream.of(
        Arguments.of(150.00, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, guru.qa.grpc.niffler.grpc.CurrencyValues.KZT, 1071.43),
        Arguments.of(34.00, guru.qa.grpc.niffler.grpc.CurrencyValues.USD, guru.qa.grpc.niffler.grpc.CurrencyValues.EUR, 31.48),
        Arguments.of(150.00, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, 150.00),
        Arguments.of(0.00, guru.qa.grpc.niffler.grpc.CurrencyValues.KZT, guru.qa.grpc.niffler.grpc.CurrencyValues.RUB, 0.00)
    );
  }

  @MethodSource
  @ParameterizedTest
  void convertSpendTo(double spend,
      guru.qa.grpc.niffler.grpc.CurrencyValues spendCurrency,
      guru.qa.grpc.niffler.grpc.CurrencyValues desiredCurrency,
      double expectedResult) {

    BigDecimal result = grpcCurrencyService.convertSpendTo(spend, spendCurrency,
        desiredCurrency, testCurrencies);

    Assertions.assertEquals(expectedResult, result.doubleValue());
  }
}