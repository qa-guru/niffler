package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.grpc.niffler.grpc.Currency;

import java.util.Objects;

public class CurrencyJson {
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("currencyRate")
    private Double currencyRate;

    public CurrencyJson(CurrencyValues currency, Double currencyRate) {
        this.currency = currency;
        this.currencyRate = currencyRate;
    }

    public CurrencyJson() {
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public static CurrencyJson fromGrpcMessage(Currency currencyMessage) {
        return new CurrencyJson(CurrencyValues.valueOf(currencyMessage.getCurrency().name()), currencyMessage.getCurrencyRate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyJson currency1 = (CurrencyJson) o;
        return currency == currency1.currency && Objects.equals(currencyRate, currency1.currencyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, currencyRate);
    }
}
