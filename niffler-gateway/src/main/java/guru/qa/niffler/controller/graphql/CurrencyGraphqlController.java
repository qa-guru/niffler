package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
public class CurrencyGraphqlController {

  private final GrpcCurrencyClient grpcCurrencyClient;

  @Autowired
  public CurrencyGraphqlController(GrpcCurrencyClient grpcCurrencyClient) {
    this.grpcCurrencyClient = grpcCurrencyClient;
  }

  @QueryMapping
  public List<CurrencyJson> currencies() {
    return grpcCurrencyClient.getAllCurrencies();
  }
}
