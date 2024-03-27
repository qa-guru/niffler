package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;

@GrpcTest
public abstract class BaseGrpcTest {

    protected static final Config CFG = Config.getConfig();

    protected static final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceStub stub;
    protected static final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub blockingStub;
    protected static final Channel channel;

    static {
        channel = ManagedChannelBuilder
                .forAddress(CFG.currencyGrpcHost(), CFG.currencyGrpcPort())
                .intercept(new GrpcConsoleInterceptor())
                .usePlaintext()
                .build();

        stub = NifflerCurrencyServiceGrpc.newStub(channel);
        blockingStub = NifflerCurrencyServiceGrpc.newBlockingStub(channel);
    }
}
