package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.model.StatisticV2Json;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface SpendClient {
  @Nonnull
  List<CategoryJson> getCategories(@Nonnull String username, boolean excludeArchived);

  @Nonnull
  CategoryJson addCategory(@Nonnull CategoryJson category);

  @Nonnull
  CategoryJson updateCategory(@Nonnull CategoryJson category);

  @Nonnull
  SpendJson getSpend(@Nonnull String id,
                     @Nonnull String username);

  @Nonnull
  List<SpendJson> getSpends(@Nonnull String username,
                            @Nullable DataFilterValues filterPeriod,
                            @Nullable CurrencyValues filterCurrency);

  @Nonnull
  Page<SpendJson> getSpendsV2(@Nonnull String username,
                              @Nonnull Pageable pageable,
                              @Nullable DataFilterValues filterPeriod,
                              @Nullable CurrencyValues filterCurrency,
                              @Nullable String searchQuery);

  PagedModel<SpendJson> getSpendsV3(@Nonnull String username,
                                    @Nonnull Pageable pageable,
                                    @Nullable DataFilterValues filterPeriod,
                                    @Nullable CurrencyValues filterCurrency,
                                    @Nullable String searchQuery);

  @Nonnull
  SpendJson addSpend(@Nonnull SpendJson spend);

  @Nonnull
  SpendJson editSpend(@Nonnull SpendJson spend);

  @Nonnull
  List<StatisticJson> statistic(@Nonnull String username,
                                @Nonnull CurrencyValues userCurrency,
                                @Nullable CurrencyValues filterCurrency,
                                @Nullable DataFilterValues filterPeriod);

  @Nonnull
  StatisticV2Json statisticV2(@Nonnull String username,
                              @Nonnull CurrencyValues statCurrency,
                              @Nullable CurrencyValues filterCurrency,
                              @Nullable DataFilterValues filterPeriod);

  void deleteSpends(@Nonnull String username, @Nonnull List<String> ids);
}
