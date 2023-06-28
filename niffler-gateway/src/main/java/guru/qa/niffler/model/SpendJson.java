package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.graphql.SpendInput;
import guru.qa.niffler.model.graphql.UpdateSpendInput;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;
import java.util.UUID;

public class SpendJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("spendDate")
    @NotNull(message = "Spend date can not be null")
    @PastOrPresent(message = "Spend date must not be future")
    private Date spendDate;
    @JsonProperty("category")
    @NotNull(message = "Category can not be null")
    @NotEmpty(message = "Category can not be empty")
    private String category;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("amount")
    @NotNull(message = "Amount can not be null")
    @DecimalMin(value = "0.01", message = "Amount should be greater than 0.01")
    private Double amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("username")
    private String username;

    public SpendJson() {
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

    public static SpendJson fromSpendInput(SpendInput input) {
        SpendJson spendJson = new SpendJson();
        spendJson.setAmount(input.getAmount());
        spendJson.setSpendDate(input.getSpendDate());
        spendJson.setCategory(input.getCategory());
        spendJson.setDescription(input.getDescription());
        return spendJson;
    }

    public static SpendJson fromUpdateSpendInput(UpdateSpendInput input) {
        SpendJson spendJson = new SpendJson();
        spendJson.setId(input.getId());
        spendJson.setAmount(input.getAmount());
        spendJson.setSpendDate(input.getSpendDate());
        spendJson.setCategory(input.getCategory());
        spendJson.setCurrency(input.getCurrency());
        spendJson.setDescription(input.getDescription());
        return spendJson;
    }
}
