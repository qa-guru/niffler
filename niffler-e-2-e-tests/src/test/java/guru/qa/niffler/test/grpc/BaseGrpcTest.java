package guru.qa.niffler.test.grpc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;

@GrpcTest
public abstract class BaseGrpcTest {

  protected static final Config CFG = Config.getInstance();

  protected static final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceStub asyncStub;
  protected static final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub blockingStub;
  protected static final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceFutureStub futureAsyncStub;
  protected static final Channel channel;

  static {
    channel = ManagedChannelBuilder
        .forAddress(CFG.currencyGrpcHost(), CFG.currencyGrpcPort())
        .intercept(new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();

    asyncStub = NifflerCurrencyServiceGrpc.newStub(channel);
    blockingStub = NifflerCurrencyServiceGrpc.newBlockingStub(channel);
    futureAsyncStub = NifflerCurrencyServiceGrpc.newFutureStub(channel);
  }
}
