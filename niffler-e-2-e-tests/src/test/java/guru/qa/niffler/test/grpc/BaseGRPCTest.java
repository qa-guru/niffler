package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.qameta.allure.grpc.AllureGrpc;

@GrpcTest
public abstract class BaseGRPCTest {

    protected static final Config CFG = Config.getConfig();

    private static final Channel channel;

    static {
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "token");

        channel = ManagedChannelBuilder
                .forAddress(CFG.currencyGrpcAddress(), CFG.currencyGrpcPort())
                .intercept(new AllureGrpc())
//                .intercept(new HeaderClientInterceptor(metadata))
                .usePlaintext()
                .build();
    }

    protected final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub nifflerCurrencyBlockingStub =
            NifflerCurrencyServiceGrpc.newBlockingStub(channel);

    protected final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceStub nifflerCurrencyStub =
            NifflerCurrencyServiceGrpc.newStub(channel);

}
