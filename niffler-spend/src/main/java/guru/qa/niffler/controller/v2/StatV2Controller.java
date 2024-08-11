package guru.qa.niffler.controller.v2;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.StatisticV2Json;
import guru.qa.niffler.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/internal/v2/stat")
public class StatV2Controller {

  private final StatService statService;

  @Autowired
  public StatV2Controller(StatService statService) {
    this.statService = statService;
  }

  @GetMapping("/total")
  public StatisticV2Json getStatistic(@RequestParam String username,
                                      @RequestParam CurrencyValues statCurrency,
                                      @RequestParam(required = false) CurrencyValues filterCurrency,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
    return statService.getV2Statistic(username, statCurrency, filterCurrency, from, to);
  }
}
