package guru.qa.niffler.service;

import guru.qa.niffler.data.repository.CurrencyRepository;
import guru.qa.niffler.model.CurrencyJson;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<CurrencyJson> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(CurrencyJson::fromEntity)
                .toList();
    }
}
