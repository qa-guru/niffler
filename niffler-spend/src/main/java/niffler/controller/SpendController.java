package niffler.controller;

import niffler.model.CurrencyValues;
import niffler.model.DataFilterValues;
import niffler.model.SpendJson;
import niffler.model.StatisticJson;
import niffler.service.SpendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class SpendController {

    private final SpendService spendService;

    @Autowired
    public SpendController(SpendService spendService) {
        this.spendService = spendService;
    }

    @GetMapping("/spends")
    public List<SpendJson> getSpends(@RequestParam String username,
                                     @RequestParam(required = false) DataFilterValues filter,
                                     @RequestParam(required = false) CurrencyValues currency) {
        return spendService.getSpendsForUser(username, filter, currency);
    }

    @GetMapping("/statistic")
    public List<StatisticJson> getStatistic(@RequestParam String username,
                                            @RequestParam(required = false) CurrencyValues currency,
                                            @RequestParam(required = false) Date from,
                                            @RequestParam(required = false) Date to) {
        return spendService.getStatistic(username, currency, from, to);
    }

    @PostMapping("/addSpend")
    @ResponseStatus(HttpStatus.CREATED)
    public SpendJson addSpend(@RequestBody SpendJson spend) {
        return spendService.saveSpendForUser(spend);
    }
}
