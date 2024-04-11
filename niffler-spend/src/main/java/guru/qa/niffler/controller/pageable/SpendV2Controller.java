package guru.qa.niffler.controller.pageable;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/internal/v2/spends")
public class SpendV2Controller {

    private final SpendService spendService;

    @Autowired
    public SpendV2Controller(SpendService spendService) {
        this.spendService = spendService;
    }

    @GetMapping("/all")
    public Page<SpendJson> getSpends(@RequestParam String username,
                                     @PageableDefault Pageable pageable,
                                     @RequestParam(required = false) CurrencyValues filterCurrency,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return spendService.getSpendsForUser(username, pageable, filterCurrency, from, to);
    }
}
