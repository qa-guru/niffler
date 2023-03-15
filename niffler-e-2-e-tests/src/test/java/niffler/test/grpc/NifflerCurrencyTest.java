package niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class NifflerCurrencyTest extends BaseGRPCTest {

    @AllureId("1000")
    @Test
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies =
                Allure.step("Get all currencies", () -> nifflerCurrencyBlockingStub.getAllCurrencies(Empty.getDefaultInstance()));
        assertEquals(4, allCurrencies.getAllCurrenciesList().size());
    }

    private Queue<CalculateResponse> sendStreamRequest(Queue<CalculateRequest> calculateRequests) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<Throwable> throwableCollector = new AtomicReference<>();
        return null;
    }
}
