package guru.qa.niffler.service;


import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.Currency;
import guru.qa.grpc.niffler.grpc.CurrencyResponse;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
