package niffler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;



public class DTOSpend {

    @JsonProperty("spendDate")
    private String spendDate;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("category")
    private String category;

    @JsonProperty("username")
    private String username;

    public String getSpendDate() {
        return spendDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

    public void setSpendDate(String spendDate) {
        this.spendDate = spendDate;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}