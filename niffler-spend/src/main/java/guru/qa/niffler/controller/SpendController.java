package guru.qa.niffler.controller;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.service.SpendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
                                     @RequestParam(required = false) CurrencyValues filterCurrency,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return spendService.getSpendsForUser(username, filterCurrency, from, to);
    }

    @GetMapping("/statistic")
    public List<StatisticJson> getStatistic(@RequestParam String username,
                                            @RequestParam CurrencyValues userCurrency,
                                            @RequestParam(required = false) CurrencyValues filterCurrency,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return spendService.getStatistic(username, userCurrency, filterCurrency, from, to);
    }

    @PostMapping("/addSpend")
    @ResponseStatus(HttpStatus.CREATED)
    public SpendJson addSpend(@RequestBody SpendJson spend) {
        return spendService.saveSpendForUser(spend);
    }

    @PatchMapping("/editSpend")
    public SpendJson editSpend(@RequestBody SpendJson spend) {
        return spendService.editSpendForUser(spend);
    }

    @DeleteMapping("/deleteSpends")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteSpends(@RequestParam String username,
                             @RequestParam List<String> ids) {
        spendService.deleteSpends(username, ids);
    }
}
