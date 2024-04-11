package guru.qa.niffler.controller;

import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.service.api.GrpcCurrencyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final GrpcCurrencyClient grpcCurrencyClient;

    @Autowired
    public CurrencyController(GrpcCurrencyClient grpcCurrencyClient) {
        this.grpcCurrencyClient = grpcCurrencyClient;
    }

    @GetMapping("/all")
    public List<CurrencyJson> getAllCurrencies() {
        return grpcCurrencyClient.getAllCurrencies();
    }
}
