package guru.qa.niffler.controller;

import guru.qa.niffler.model.CurrencyCalculateJson;
import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/getAllCurrencies")
    public List<CurrencyJson> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @PostMapping("/calculate")
    public CurrencyCalculateJson getAllCurrencies(@RequestBody CurrencyCalculateJson currencyCalculate) {
        return new CurrencyCalculateJson();
    }
}
