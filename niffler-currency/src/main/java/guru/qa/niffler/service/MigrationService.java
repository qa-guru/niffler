package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyEntity;
import guru.qa.niffler.data.repository.CurrencyRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MigrationService {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationService.class);

  private final CurrencyRepository currencyRepository;

  @Autowired
  public MigrationService(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }

  /**
   * only for V2__currency_symbols.sql
   */
  @Transactional
  @PostConstruct
  public void addCurrencySymbols() {
    List<CurrencyEntity> currencies = currencyRepository.findAll();
    for (CurrencyEntity currency : currencies) {
      if (currency.getSymbol() == null) {
        try {
          ClassPathResource svg = new ClassPathResource(currency.getCurrency().symbolResource);
          currency.setSymbol(svg.getContentAsByteArray());
          currencyRepository.save(currency);
          LOG.info("### Currency symbol added: {}", currency.getId());
        } catch (Exception e) {
          LOG.error("### Error while adding symbol for Currency :{}", currency.getId());
        }
      }
    }
  }
}
