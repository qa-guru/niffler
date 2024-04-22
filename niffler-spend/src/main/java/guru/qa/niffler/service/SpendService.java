package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.SpendNotFoundException;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class SpendService {

    private final SpendRepository spendRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SpendService(SpendRepository spendRepository, CategoryRepository categoryRepository) {
        this.spendRepository = spendRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public @Nonnull
    SpendJson saveSpendForUser(@Nonnull SpendJson spend) {
        final String username = spend.username();
        final String category = spend.category();

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(username);
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setDescription(spend.description());
        spendEntity.setAmount(spend.amount());

        CategoryEntity categoryEntity = categoryRepository.findByUsernameAndCategory(username, category)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Can`t find category by given name: " + category
                ));

        spendEntity.setCategory(categoryEntity);
        return SpendJson.fromEntity(spendRepository.save(spendEntity));
    }

    @Transactional
    public @Nonnull
    SpendJson editSpendForUser(@Nonnull SpendJson spend) {
        return spendRepository.findById(spend.id()).map(
                spendEntity -> {
                    final String category = spend.category();
                    CategoryEntity categoryEntity = categoryRepository.findByUsernameAndCategory(spend.username(), category)
                            .orElseThrow(() -> new CategoryNotFoundException(
                                    "Can`t find category by given name: " + category
                            ));

                    spendEntity.setSpendDate(spend.spendDate());
                    spendEntity.setCategory(categoryEntity);
                    spendEntity.setAmount(spend.amount());
                    spendEntity.setDescription(spend.description());
                    return SpendJson.fromEntity(spendRepository.save(spendEntity));
                }
        ).orElseThrow(() -> new SpendNotFoundException(
                "Can`t find spend by given id: " + spend.id()
        ));
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<SpendJson> getSpendsForUser(@Nonnull String username,
                                     @Nullable CurrencyValues filterCurrency,
                                     @Nullable Date dateFrom,
                                     @Nullable Date dateTo) {
        return getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo)
                .stream()
                .map(SpendJson::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public @Nonnull
    Page<SpendJson> getSpendsForUser(@Nonnull String username,
                                     @Nonnull Pageable pageable,
                                     @Nullable CurrencyValues filterCurrency,
                                     @Nullable Date dateFrom,
                                     @Nullable Date dateTo) {
        return getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo, pageable)
                .map(SpendJson::fromEntity);
    }

    @Transactional
    public void deleteSpends(@Nonnull String username, @Nonnull List<String> ids) {
        spendRepository.deleteByUsernameAndIdIn(
                username,
                ids.stream().map(UUID::fromString).toList()
        );
    }

    @Nonnull
    List<SpendEntity> getSpendsEntityForUser(@Nonnull String username,
                                             @Nullable CurrencyValues filterCurrency,
                                             @Nullable Date dateFrom,
                                             @Nullable Date dateTo) {
        dateTo = dateTo == null
                ? new Date()
                : dateTo;

        List<SpendEntity> spends;
        if (dateFrom == null) {
            spends = filterCurrency != null
                    ?
                    spendRepository.findAllByUsernameAndCurrencyAndSpendDateLessThanEqual(
                            username, filterCurrency, dateTo
                    )
                    :
                    spendRepository.findAllByUsernameAndSpendDateLessThanEqual(
                            username, dateTo
                    );
        } else {
            spends = filterCurrency != null
                    ?
                    spendRepository.findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
                            username, filterCurrency, dateFrom, dateTo
                    )
                    :
                    spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
                            username, dateFrom, dateTo
                    );
        }
        return spends;
    }

    @Nonnull
    Page<SpendEntity> getSpendsEntityForUser(@Nonnull String username,
                                             @Nullable CurrencyValues filterCurrency,
                                             @Nullable Date dateFrom,
                                             @Nullable Date dateTo,
                                             @Nonnull Pageable pageable) {
        dateTo = dateTo == null
                ? new Date()
                : dateTo;

        Page<SpendEntity> spends;
        if (dateFrom == null) {
            spends = filterCurrency != null
                    ?
                    spendRepository.findAllByUsernameAndCurrencyAndSpendDateLessThanEqual(
                            username, filterCurrency, dateTo, pageable
                    )
                    :
                    spendRepository.findAllByUsernameAndSpendDateLessThanEqual(
                            username, dateTo, pageable
                    );
        } else {
            spends = filterCurrency != null
                    ?
                    spendRepository.findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
                            username, filterCurrency, dateFrom, dateTo, pageable
                    )
                    :
                    spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
                            username, dateFrom, dateTo, pageable
                    );
        }
        return spends;
    }
}
