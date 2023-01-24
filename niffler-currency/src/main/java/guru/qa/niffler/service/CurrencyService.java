package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.repository.CurrencyRepository;
import guru.qa.niffler.model.CurrencyCalculateJson;
import guru.qa.niffler.model.CurrencyJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<CurrencyJson> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(CurrencyJson::fromEntity)
                .collect(Collectors.toList());
    }

    public CurrencyCalculateJson calculate(CurrencyCalculateJson source) {
        CurrencyEntity original = currencyRepository.findByCurrency(source.getOriginalCurrency().toString());
        CurrencyEntity desired = currencyRepository.findByCurrency(source.getDesiredCurrency().toString());
        if (original.getCurrency() != CurrencyValues.USD) {
            CurrencyEntity usd = currencyRepository.findByCurrency(CurrencyValues.USD.toString());
        }
        return null;
    }
}
