package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StatisticByCategoryJson {
    @JsonProperty("category")
    private String category;
    @JsonProperty("total")
    private Double total;
    @JsonProperty("spends")
    private List<SpendJson> spends;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<SpendJson> getSpends() {
        return spends;
    }

    public void setSpends(List<SpendJson> spends) {
        this.spends = spends;
    }
}
