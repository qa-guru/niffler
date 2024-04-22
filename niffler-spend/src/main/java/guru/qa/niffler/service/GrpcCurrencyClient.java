package guru.qa.niffler.service;


import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf;

@Component
public class GrpcCurrencyClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCurrencyClient.class);

    private NifflerCurrencyServiceBlockingStub nifflerCurrencyServiceStub;

    @GrpcClient("grpcCurrencyClient")
    public void setNifflerCurrencyServiceStub(NifflerCurrencyServiceBlockingStub nifflerCurrencyServiceStub) {
        this.nifflerCurrencyServiceStub = nifflerCurrencyServiceStub;
    }

    public @Nonnull
    BigDecimal calculate(double amount,
                         @Nonnull CurrencyValues spendCurrency,
                         @Nonnull CurrencyValues desiredCurrency) {
        return BigDecimal.valueOf(
                nifflerCurrencyServiceStub.calculateRate(
                        CalculateRequest.newBuilder()
                                .setAmount(amount)
                                .setSpendCurrency(valueOf(spendCurrency.name()))
                                .setDesiredCurrency(valueOf(desiredCurrency.name()))
                                .build()
                ).getCalculatedAmount()
        );
    }
}
