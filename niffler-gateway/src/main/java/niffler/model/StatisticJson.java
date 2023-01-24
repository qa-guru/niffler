package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class StatisticJson {
    @JsonProperty("dateFrom")
    private Date dateFrom;
    @JsonProperty("dateTo")
    private Date dateTo;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("total")
    private Double total;
    @JsonProperty("categoryStatistics")
    private List<StatisticByCategoryJson> categoryStatistics;

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<StatisticByCategoryJson> getCategoryStatistics() {
        return categoryStatistics;
    }

    public void setCategoryStatistics(List<StatisticByCategoryJson> categoryStatistics) {
        this.categoryStatistics = categoryStatistics;
    }
}
