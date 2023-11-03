package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import java.util.Date;
import java.util.UUID;

public class Spend7Json {
    @JsonProperty("id")
    private UUID id;
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

    public Spend7Json() {
    }

    public Spend7Json(String spendDate, String category, String currency, String amount, String description, String username) {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "SpendJson{" +
            "id=" + id +
            ", spendDate=" + spendDate +
            ", category='" + category + '\'' +
            ", currency=" + currency +
            ", amount=" + amount +
            ", description='" + description + '\'' +
            ", username='" + username + '\'' +
            '}';
    }
}
