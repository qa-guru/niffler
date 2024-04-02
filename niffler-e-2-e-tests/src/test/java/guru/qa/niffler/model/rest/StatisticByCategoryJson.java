package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record StatisticByCategoryJson(
        @JsonProperty("category")
        String category,
        @JsonProperty("total")
        Double total,
        @JsonProperty("totalInUserDefaultCurrency")
        Double totalInUserDefaultCurrency,
        @JsonProperty("spends")
        List<SpendJson> spends) {

}
