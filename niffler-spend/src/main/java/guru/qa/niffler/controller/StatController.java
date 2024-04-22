package guru.qa.niffler.controller;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/internal/stat")
public class StatController {

    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/total")
    public List<StatisticJson> getStatistic(@RequestParam String username,
                                            @RequestParam CurrencyValues userCurrency,
                                            @RequestParam(required = false) CurrencyValues filterCurrency,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return statService.getStatistic(username, userCurrency, filterCurrency, from, to);
    }
}
