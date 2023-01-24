package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.CurrencyValues;

import java.util.Objects;

public class CurrencyCalculateJson {
    @JsonProperty("originalCurrency")
    private CurrencyValues originalCurrency;
    @JsonProperty("desiredCurrency")
    private CurrencyValues desiredCurrency;
    @JsonProperty("originalAmount")
    private Double originalAmount;
    @JsonProperty("desiredAmount")
    private Double desiredAmount;

    public CurrencyCalculateJson(CurrencyValues originalCurrency, CurrencyValues desiredCurrency, Double originalAmount, Double desiredAmount) {
        this.originalCurrency = originalCurrency;
        this.desiredCurrency = desiredCurrency;
        this.originalAmount = originalAmount;
        this.desiredAmount = desiredAmount;
    }

    public CurrencyCalculateJson(CurrencyValues originalCurrency, CurrencyValues desiredCurrency, Double originalAmount) {
        this.originalCurrency = originalCurrency;
        this.desiredCurrency = desiredCurrency;
        this.originalAmount = originalAmount;
    }

    public CurrencyCalculateJson() {
    }

    public CurrencyValues getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(CurrencyValues originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public CurrencyValues getDesiredCurrency() {
        return desiredCurrency;
    }

    public void setDesiredCurrency(CurrencyValues desiredCurrency) {
        this.desiredCurrency = desiredCurrency;
    }

    public Double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Double getDesiredAmount() {
        return desiredAmount;
    }

    public void setDesiredAmount(Double desiredAmount) {
        this.desiredAmount = desiredAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyCalculateJson that = (CurrencyCalculateJson) o;
        return originalCurrency == that.originalCurrency && desiredCurrency == that.desiredCurrency && Objects.equals(originalAmount, that.originalAmount) && Objects.equals(desiredAmount, that.desiredAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalCurrency, desiredCurrency, originalAmount, desiredAmount);
    }
}
