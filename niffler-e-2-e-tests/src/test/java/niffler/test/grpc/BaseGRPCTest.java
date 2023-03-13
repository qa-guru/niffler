package niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.qameta.allure.grpc.AllureGrpc;
import niffler.config.Config;

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

    protected NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub nifflerCurrencyBlockingStub =
            NifflerCurrencyServiceGrpc.newBlockingStub(channel);

    protected NifflerCurrencyServiceGrpc.NifflerCurrencyServiceStub nifflerCurrencyStub =
            NifflerCurrencyServiceGrpc.newStub(channel);

}
