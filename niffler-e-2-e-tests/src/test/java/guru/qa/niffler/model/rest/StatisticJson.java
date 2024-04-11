package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public record StatisticJson(
        @JsonProperty("dateFrom")
        Date dateFrom,
        @JsonProperty("dateTo")
        Date dateTo,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("total")
        Double total,
        @JsonProperty("userDefaultCurrency")
        CurrencyValues userDefaultCurrency,
        @JsonProperty("totalInUserDefaultCurrency")
        Double totalInUserDefaultCurrency,
        @JsonProperty("categoryStatistics")
        List<StatisticByCategoryJson> categoryStatistics) {

}
