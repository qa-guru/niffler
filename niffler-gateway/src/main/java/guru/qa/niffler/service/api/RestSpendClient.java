package guru.qa.niffler.service.api;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.model.StatisticV2Json;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.service.utils.HttpQueryPaginationAndSort;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class RestSpendClient {

  private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

  private final RestTemplate restTemplate;
  private final String nifflerSpendApiUri;

  @Autowired
  public RestSpendClient(RestTemplate restTemplate,
                         @Value("${niffler-spend.base-uri}") String nifflerSpendUri) {
    this.restTemplate = restTemplate;
    this.nifflerSpendApiUri = nifflerSpendUri + "/internal";
  }

  public @Nonnull
  List<CategoryJson> getCategories(@Nonnull String username, boolean excludeArchived) {
    return List.of(Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/categories/all?username={username}&excludeArchived={excludeArchived}",
            CategoryJson[].class,
            username,
            excludeArchived
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST List<CategoryJson> response is given [/categories/all/ Route]")));
  }

  public @Nonnull
  CategoryJson addCategory(@Nonnull CategoryJson category) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerSpendApiUri + "/categories/add",
            category,
            CategoryJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST CategoryJson response is given [/categories/add/ Route]"));
  }

  public @Nonnull
  CategoryJson updateCategory(@Nonnull CategoryJson category) {
    return Optional.ofNullable(
        restTemplate.patchForObject(
            nifflerSpendApiUri + "/categories/update",
            category,
            CategoryJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST CategoryJson response is given [/categories/update/ Route]"));
  }

  public @Nonnull
  SpendJson getSpend(@Nonnull String id,
                     @Nonnull String username) {
    return Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/spends/{id}?username={username}",
            SpendJson.class,
            id,
            username
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST SpendJson response is given [/spend/{id}/ Route]"));
  }

  public @Nonnull
  List<SpendJson> getSpends(@Nonnull String username,
                            @Nullable DataFilterValues filterPeriod,
                            @Nullable CurrencyValues filterCurrency) {
    return List.of(Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/spends/all?username={username}&from={from}&to={to}&filterCurrency={filterCurrency}",
            SpendJson[].class,
            username,
            filterPeriod != null ? dateFormat(filterDate(filterPeriod)) : null,
            filterPeriod != null ? dateFormat(new Date()) : null,
            filterCurrency != null ? filterCurrency.name() : null
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST List<SpendJson> response is given [/spends/all/ Route]")));
  }

  @SuppressWarnings("unchecked")
  public @Nonnull
  Page<SpendJson> getSpends(@Nonnull String username,
                            @Nonnull Pageable pageable,
                            @Nullable DataFilterValues filterPeriod,
                            @Nullable CurrencyValues filterCurrency,
                            @Nullable String searchQuery) {
    return Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/v2/spends/all?username={username}&from={from}&to={to}&filterCurrency={filterCurrency}&searchQuery={searchQuery}"
            + new HttpQueryPaginationAndSort(pageable),
            RestPage.class,
            username,
            filterPeriod != null ? dateFormat(filterDate(filterPeriod)) : null,
            filterPeriod != null ? dateFormat(new Date()) : null,
            filterCurrency != null ? filterCurrency.name() : null,
            searchQuery
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST Page<SpendJson> response is given [/v2/spends/all/ Route]"));
  }

  public @Nonnull
  SpendJson addSpend(@Nonnull SpendJson spend) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerSpendApiUri + "/spends/add",
            spend,
            SpendJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST SpendJson response is given [/spends/add/ Route]"));
  }

  public @Nonnull
  SpendJson editSpend(@Nonnull SpendJson spend) {
    return Optional.ofNullable(
        restTemplate.patchForObject(
            nifflerSpendApiUri + "/spends/edit",
            spend,
            SpendJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST SpendJson response is given [/spends/edit/ Route]"));
  }

  public @Nonnull
  List<StatisticJson> statistic(@Nonnull String username,
                                @Nonnull CurrencyValues userCurrency,
                                @Nullable CurrencyValues filterCurrency,
                                @Nullable DataFilterValues filterPeriod) {
    return List.of(Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/stat/total?username={username}&userCurrency={userCurrency}&from={from}&to={to}&filterCurrency={filterCurrency}",
            StatisticJson[].class,
            username,
            userCurrency.name(),
            filterPeriod != null ? dateFormat(filterDate(filterPeriod)) : null,
            filterPeriod != null ? dateFormat(new Date()) : null,
            filterCurrency != null ? filterCurrency.name() : null
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST List<StatisticJson> response is given [/v2/stat/total Route]")));
  }

  public @Nonnull
  StatisticV2Json statisticV2(@Nonnull String username,
                              @Nonnull CurrencyValues statCurrency,
                              @Nullable CurrencyValues filterCurrency,
                              @Nullable DataFilterValues filterPeriod) {
    return Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/v2/stat/total?username={username}&statCurrency={statCurrency}&from={from}&to={to}&filterCurrency={filterCurrency}",
            StatisticV2Json.class,
            username,
            statCurrency.name(),
            filterPeriod != null ? dateFormat(filterDate(filterPeriod)) : null,
            filterPeriod != null ? dateFormat(new Date()) : null,
            filterCurrency != null ? filterCurrency.name() : null
        )).orElseThrow(() -> new NoRestResponseException("No REST StatisticV2Json response is given [/v2/stat/total Route]"));
  }

  public void deleteSpends(@Nonnull String username, @Nonnull List<String> ids) {
    restTemplate.delete(
        nifflerSpendApiUri + "/spends/remove?username={username}&ids={ids}",
        username,
        String.join(",", ids)
    );
  }

  private @Nonnull
  String dateFormat(@Nonnull Date date, @Nonnull String pattern) {
    return new SimpleDateFormat(pattern).format(date);
  }

  private @Nonnull
  String dateFormat(@Nonnull Date date) {
    return dateFormat(date, DEFAULT_DATE_FORMAT);
  }

  private @Nonnull
  Date filterDate(@Nonnull DataFilterValues filter) {
    Date currentDate = new Date();
    return switch (filter) {
      case TODAY -> currentDate;
      case WEEK -> addDaysToDate(currentDate, Calendar.WEEK_OF_MONTH, -1);
      case MONTH -> addDaysToDate(currentDate, Calendar.MONTH, -1);
    };
  }

  private @Nonnull
  Date addDaysToDate(@Nonnull Date date, int selector, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(selector, days);
    return cal.getTime();
  }
}
