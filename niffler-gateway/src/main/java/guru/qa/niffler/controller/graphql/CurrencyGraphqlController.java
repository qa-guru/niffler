package guru.qa.niffler.controller.graphql;

import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
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
