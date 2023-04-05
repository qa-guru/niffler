package niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NifflerCurrencyTest extends BaseGRPCTest {

    @AllureId("300001")
    @Test
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies =
                Allure.step("Get all currencies", () -> nifflerCurrencyBlockingStub.getAllCurrencies(Empty.getDefaultInstance()));
        assertEquals(4, allCurrencies.getAllCurrenciesList().size());
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
    @ParameterizedTest
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
