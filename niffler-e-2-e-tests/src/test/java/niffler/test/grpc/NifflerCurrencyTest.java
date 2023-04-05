package niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.Currency;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NifflerCurrencyTest extends BaseGRPCTest {

    @Test
    @DisplayName("gRPC: Сервис niffler-currency должен возвращать 4 валюты")
    @AllureId("300001")
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies =
                Allure.step("Get all currencies", () -> nifflerCurrencyBlockingStub.getAllCurrencies(Empty.getDefaultInstance()));
        final List<Currency> currenciesList = allCurrencies.getAllCurrenciesList();
        assertEquals(4, allCurrencies.getAllCurrenciesList().size());
        assertEquals(CurrencyValues.RUB, currenciesList.get(0).getCurrency());
        assertEquals(0.015, currenciesList.get(0).getCurrencyRate());
        assertEquals(CurrencyValues.KZT, currenciesList.get(1).getCurrency());
        assertEquals(0.0021, currenciesList.get(1).getCurrencyRate());
        assertEquals(CurrencyValues.EUR, currenciesList.get(2).getCurrency());
        assertEquals(1.08, currenciesList.get(2).getCurrencyRate());
        assertEquals(CurrencyValues.USD, currenciesList.get(3).getCurrency());
        assertEquals(1.0, currenciesList.get(3).getCurrencyRate());
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
    @ParameterizedTest(name = "gRPC: При пересчете из {0} в {1} суммы {2} сервис niffler-currency должен возвращать результат {3}")
    void calculateRateTest(CurrencyValues spendCurrency,
                           CurrencyValues desiredCurrency,
                           double amount,
                           double expected) {
        CalculateRequest request = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        CalculateResponse calculateResponse = Allure.step("Get all currencies", () -> nifflerCurrencyBlockingStub.calculateRate(request));
        assertEquals(expected, calculateResponse.getCalculatedAmount());
    }
}
