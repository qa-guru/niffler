package niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.Currency;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("[gRPC][niffler-currency]: Валюты")
@DisplayName("[gRPC][niffler-currency]: Валюты")
public class NifflerCurrencyTest extends BaseGRPCTest {

    @Test
    @DisplayName("gRPC: Сервис niffler-currency должен возвращать 4 валюты")
    @AllureId("300001")
    @Tag("gRPC")
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies = step("Get all currencies", () ->
                nifflerCurrencyBlockingStub.getAllCurrencies(Empty.getDefaultInstance())
        );
        final List<Currency> currenciesList = allCurrencies.getAllCurrenciesList();

        step("Check that response contains 4 currencies", () ->
                assertEquals(4, allCurrencies.getAllCurrenciesList().size())
        );
        step("Check RUB currency rate", () -> {
            assertEquals(CurrencyValues.RUB, currenciesList.get(0).getCurrency());
            assertEquals(0.015, currenciesList.get(0).getCurrencyRate());
        });
        step("Check KZT currency rate", () -> {
            assertEquals(CurrencyValues.KZT, currenciesList.get(1).getCurrency());
            assertEquals(0.0021, currenciesList.get(1).getCurrencyRate());
        });
        step("Check EUR currency rate", () -> {
            assertEquals(CurrencyValues.EUR, currenciesList.get(2).getCurrency());
            assertEquals(1.08, currenciesList.get(2).getCurrencyRate());
        });
        step("Check USD currency rate", () -> {
            assertEquals(CurrencyValues.USD, currenciesList.get(3).getCurrency());
            assertEquals(1.0, currenciesList.get(3).getCurrencyRate());
        });
    }

    static Stream<Arguments> calculateRateTest() {
        return Stream.of(
                Arguments.of(CurrencyValues.USD, CurrencyValues.RUB, 100.0, 6666.67),
                Arguments.of(CurrencyValues.USD, CurrencyValues.USD, 100.0, 100.0),
                Arguments.of(CurrencyValues.RUB, CurrencyValues.USD, 100.0, 1.5)
        );
    }

    @AllureId("300002")
    @MethodSource
    @DisplayName("gRPC: Пересчет курсов валют в niffler-currency")
    @ParameterizedTest(name = "При пересчете из {0} в {1} суммы {2} должен возвращаться результат {3}")
    @Tag("gRPC")
    void calculateRateTest(CurrencyValues spendCurrency,
                           CurrencyValues desiredCurrency,
                           double amount,
                           double expected) {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        final CalculateResponse calculateResponse = step("Get all currencies", () ->
                nifflerCurrencyBlockingStub.calculateRate(request)
        );

        step("Check calculated rate", () ->
                assertEquals(expected, calculateResponse.getCalculatedAmount())
        );
    }
}
