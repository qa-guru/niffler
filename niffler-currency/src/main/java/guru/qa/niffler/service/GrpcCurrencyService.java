package guru.qa.niffler.service;


import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.CalculateRequest;
import guru.qa.grpc.niffler.grpc.CalculateResponse;
import guru.qa.grpc.niffler.grpc.Currency;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@GrpcService
public class GrpcCurrencyService extends NifflerCurrencyServiceGrpc.NifflerCurrencyServiceImplBase {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public GrpcCurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void getAllCurrencies(Empty request, StreamObserver<CurrencyResponse> responseObserver) {
        List<CurrencyEntity> all = currencyRepository.findAll();

        CurrencyResponse response = CurrencyResponse.newBuilder()
                .addAllAllCurrencies(all.stream().map(e -> Currency.newBuilder()
                                .setCurrency(CurrencyValues.valueOf(e.getCurrency().name()))
                                .setCurrencyRate(e.getCurrencyRate())
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void calculateRate(CalculateRequest request, StreamObserver<CalculateResponse> responseObserver) {
        BigDecimal result = convertSpendTo(
                request.getAmount(),
                request.getSpendCurrency(),
                request.getDesiredCurrency(),
                currencyRepository.findAll()
        );

        responseObserver.onNext(CalculateResponse.newBuilder()
                .setCalculatedAmount(result.doubleValue())
                .build());
        responseObserver.onCompleted();
    }

    private @Nonnull
    BigDecimal convertSpendTo(double spend,
                              @Nonnull CurrencyValues spendCurrency,
                              @Nonnull CurrencyValues desiredCurrency,
                              @Nonnull List<CurrencyEntity> currencyRates) {
        BigDecimal spendInUsd = spendCurrency == CurrencyValues.USD
                ? BigDecimal.valueOf(spend)
                : BigDecimal.valueOf(spend).multiply(courseForCurrency(spendCurrency, currencyRates));

        return spendInUsd.divide(courseForCurrency(desiredCurrency, currencyRates), 2, RoundingMode.HALF_UP);
    }

    private @Nonnull
    BigDecimal courseForCurrency(@Nonnull CurrencyValues currency,
                                 @Nonnull List<CurrencyEntity> currencyRates) {
        return BigDecimal.valueOf(
                currencyRates.stream()
                        .filter(cr -> cr.getCurrency().name().equals(currency.name()))
                        .findFirst()
                        .orElseThrow()
                        .getCurrencyRate()
        );
    }
}
