package niffler.service;


import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import niffler.model.CurrencyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Component
public class GrpcCurrencyClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCurrencyClient.class);

    @GrpcClient("grpcCurrencyClient")
    private NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub nifflerCurrencyServiceStub;

    public @Nonnull
    BigDecimal calculate(double amount,
                         @Nonnull CurrencyValues spendCurrency,
                         @Nonnull CurrencyValues desiredCurrency) {
        try {
            return BigDecimal.valueOf(nifflerCurrencyServiceStub.calculateRate(CalculateRequest.newBuilder()
                    .setAmount(amount)
                    .setSpendCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(spendCurrency.name()))
                    .setDesiredCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(desiredCurrency.name()))
                    .build()).getCalculatedAmount());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
