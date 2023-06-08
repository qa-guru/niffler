package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.GrpcTest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

@GrpcTest
public class BaseGrpcTest {

  protected static final Config CFG = Config.getConfig();
  protected static final Empty EMPTY = Empty.getDefaultInstance();
  private static Channel channel;

  static {
    channel = ManagedChannelBuilder
        .forAddress(CFG.getCurrencyGrpcAddress(), CFG.getCurrencyGrpcPort())
        .intercept(new AllureGrpc())
        .usePlaintext()
        .build();
  }

  protected final NifflerCurrencyServiceBlockingStub currencyStub
      = NifflerCurrencyServiceGrpc.newBlockingStub(channel);
}
