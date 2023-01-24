package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import niffler.data.SpendEntity;

import java.util.Date;

public class SpendJson {
    @JsonProperty("spendDate")
    private Date spendDate;
    @JsonProperty("category")
    private String category;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("username")
    private String username;

    public SpendJson() {
    }

    public Date getSpendDate() {
        return spendDate;
    }

    public void setSpendDate(Date spendDate) {
        this.spendDate = spendDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static SpendJson fromEntity(SpendEntity entity) {
        SpendJson spend = new SpendJson();
        spend.setSpendDate(entity.getSpendDate());
        spend.setCategory(entity.getCategory().getDescription());
        spend.setCurrency(entity.getCurrency());
        spend.setAmount(entity.getAmount());
        spend.setDescription(entity.getDescription());
        spend.setUsername(entity.getUsername());
        return spend;
    }
}
