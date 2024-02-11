package guru.qa.niffler.test.grpc;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.EUR;
import static guru.qa.grpc.niffler.grpc.CurrencyValues.KZT;
import static guru.qa.grpc.niffler.grpc.CurrencyValues.RUB;
import static guru.qa.grpc.niffler.grpc.CurrencyValues.USD;

import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.Currency;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class NifflerCurrencyGrpcTest extends BaseGrpcTest {

  @Test
  void getAllCurrenciesTest() {
    CurrencyResponse allCurrencies = currencyStub.getAllCurrencies(EMPTY);
    List<Currency> currenciesList = allCurrencies.getCurrenciesList();
    Assertions.assertEquals(RUB, currenciesList.get(0).getCurrency());
    Assertions.assertEquals(CurrencyValues.KZT, currenciesList.get(1).getCurrency());
    Assertions.assertEquals(EUR, currenciesList.get(2).getCurrency());
    Assertions.assertEquals(USD, currenciesList.get(3).getCurrency());
    Assertions.assertEquals(4, currenciesList.size());
  }

  private static Stream<Arguments> getCurrencyAndAmountData() {
    return Stream.of(
        Arguments.of(RUB, KZT, 100.0, 714.29),
        Arguments.of(RUB, USD, 100.0, 1.5),
        Arguments.of(RUB, EUR, 100.0, 1.39),
        Arguments.of(RUB, RUB, 100.0, 100.0),

        Arguments.of(KZT, RUB, 100.0, 14.0),
        Arguments.of(USD, RUB, 100.0, 6666.67),
        Arguments.of(EUR, RUB, 100.0, 7200.0),

        Arguments.of(KZT, RUB, -100.0, -14.0),
        Arguments.of(USD, RUB, -100.0, -6666.67),
        Arguments.of(EUR, RUB, -100.0, -7200.0),
        Arguments.of(RUB, RUB, -100.0, -100.0),

        Arguments.of(KZT, RUB, 0.0, 0.0),
        Arguments.of(USD, RUB, 0.0, 0.0),
        Arguments.of(EUR, RUB, 0.0, 0.0)
    );
  }

  @MethodSource("getCurrencyAndAmountData")
  @ParameterizedTest
  void calculateRateTest(
      CurrencyValues firstCurrency, CurrencyValues secondCurrency,
      Double amount, Double expectedAmount
  ) {
    final CalculateRequest cr = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setSpendCurrency(firstCurrency)
        .setDesiredCurrency(secondCurrency)
        .build();

    CalculateResponse response = currencyStub.calculateRate(cr);

    Assertions.assertEquals(expectedAmount, response.getCalculatedAmount());
  }
}
