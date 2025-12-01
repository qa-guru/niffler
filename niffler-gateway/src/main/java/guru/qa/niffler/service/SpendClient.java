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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface SpendClient {
  @Nonnull
  List<CategoryJson> getCategories(String username, boolean excludeArchived);

  @Nonnull
  CategoryJson addCategory(CategoryJson category);

  @Nonnull
  CategoryJson updateCategory(CategoryJson category);

  @Nonnull
  SpendJson getSpend(String id,
                     String username);

  @Nonnull
  List<SpendJson> getSpends(String username,
                            @Nullable DataFilterValues filterPeriod,
                            @Nullable CurrencyValues filterCurrency);

  @Nonnull
  Page<SpendJson> getSpendsV2(String username,
                              Pageable pageable,
                              @Nullable DataFilterValues filterPeriod,
                              @Nullable CurrencyValues filterCurrency,
                              @Nullable String searchQuery);

  PagedModel<SpendJson> getSpendsV3(String username,
                                    Pageable pageable,
                                    @Nullable DataFilterValues filterPeriod,
                                    @Nullable CurrencyValues filterCurrency,
                                    @Nullable String searchQuery);

  @Nonnull
  SpendJson addSpend(SpendJson spend);

  @Nonnull
  SpendJson editSpend(SpendJson spend);

  @Nonnull
  List<StatisticJson> statistic(String username,
                                CurrencyValues userCurrency,
                                @Nullable CurrencyValues filterCurrency,
                                @Nullable DataFilterValues filterPeriod);

  @Nonnull
  StatisticV2Json statisticV2(String username,
                              CurrencyValues statCurrency,
                              @Nullable CurrencyValues filterCurrency,
                              @Nullable DataFilterValues filterPeriod);

  void deleteSpends(String username, List<String> ids);

  byte[] exportToCsv(String username);
}
