package guru.qa.niffler.service.api;


import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.niffler.model.CurrencyJson;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Component
@ParametersAreNonnullByDefault
public class GrpcCurrencyClient {

  private static final Empty EMPTY = Empty.getDefaultInstance();

  private final NifflerCurrencyServiceGrpc.NifflerCurrencyServiceBlockingStub nifflerCurrencyServiceStub;

  @Autowired
  public GrpcCurrencyClient(GrpcChannelFactory channels) {
    this.nifflerCurrencyServiceStub = NifflerCurrencyServiceGrpc.newBlockingStub(
        channels.createChannel("grpcCurrencyClient"));
  }

  public @Nonnull
  List<CurrencyJson> getAllCurrencies() {
    return nifflerCurrencyServiceStub.getAllCurrencies(EMPTY).getAllCurrenciesList()
        .stream()
        .map(CurrencyJson::fromGrpcMessage)
        .toList();
  }
}
