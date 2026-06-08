package guru.qa.niffler.service;


import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;

import static guru.qa.niffler.grpc.CurrencyValues.valueOf;

@Component
@ParametersAreNonnullByDefault
public class GrpcCurrencyClient {

  private static final Logger LOG = LoggerFactory.getLogger(GrpcCurrencyClient.class);

  private final NifflerCurrencyServiceBlockingStub nifflerCurrencyServiceStub;

  @Autowired
  public GrpcCurrencyClient(GrpcChannelFactory channels) {
    this.nifflerCurrencyServiceStub = NifflerCurrencyServiceGrpc.newBlockingStub(
        channels.createChannel("grpcCurrencyClient"));
  }

  public @Nonnull
  BigDecimal calculate(double amount,
                       CurrencyValues spendCurrency,
                       CurrencyValues desiredCurrency) {
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
