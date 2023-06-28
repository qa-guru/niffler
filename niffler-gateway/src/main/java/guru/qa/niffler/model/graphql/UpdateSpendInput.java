package guru.qa.niffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;
import java.util.UUID;

public class UpdateSpendInput {
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

}
