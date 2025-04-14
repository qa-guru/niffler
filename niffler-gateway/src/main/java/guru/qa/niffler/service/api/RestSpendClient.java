package guru.qa.niffler.service.api;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.model.StatisticV2Json;
import guru.qa.niffler.model.page.PagedModelJson;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.utils.HttpQueryPaginationAndSort;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.service.utils.DateUtils.dateFormat;
import static guru.qa.niffler.service.utils.DateUtils.filterDate;

@Component
@ParametersAreNonnullByDefault
public class RestSpendClient implements SpendClient {

  private final RestTemplate restTemplate;
  private final String nifflerSpendApiUri;

  @Autowired
  public RestSpendClient(RestTemplate restTemplate,
                         @Value("${niffler-spend.base-uri}") String nifflerSpendUri) {
    this.restTemplate = restTemplate;
    this.nifflerSpendApiUri = nifflerSpendUri + "/internal";
  }

  @Nonnull
  @Override
  public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
    return List.of(Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/categories/all?username={username}&excludeArchived={excludeArchived}",
            CategoryJson[].class,
            username,
            excludeArchived
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST List<CategoryJson> response is given [/categories/all/ Route]")));
  }

  @Nonnull
  @Override
  public CategoryJson addCategory(CategoryJson category) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerSpendApiUri + "/categories/add",
            category,
            CategoryJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST CategoryJson response is given [/categories/add/ Route]"));
  }

  @Nonnull
  @Override
  public CategoryJson updateCategory(CategoryJson category) {
    return Optional.ofNullable(
        restTemplate.patchForObject(
            nifflerSpendApiUri + "/categories/update",
            category,
            CategoryJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST CategoryJson response is given [/categories/update/ Route]"));
  }

  @Nonnull
  @Override
  public SpendJson getSpend(String id,
                            String username) {
    return Optional.ofNullable(
        restTemplate.getForObject(
            nifflerSpendApiUri + "/spends/{id}?username={username}",
            SpendJson.class,
            id,
            username
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST SpendJson response is given [/spend/{id}/ Route]"));
  }

  @Nonnull
  @Override
  public List<SpendJson> getSpends(String username,
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
  @Nonnull
  @Override
  public Page<SpendJson> getSpendsV2(String username,
                                     Pageable pageable,
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

  @Nonnull
  @Override
  public PagedModel<SpendJson> getSpendsV3(String username,
                                           Pageable pageable,
                                           @Nullable DataFilterValues filterPeriod,
                                           @Nullable CurrencyValues filterCurrency,
                                           @Nullable String searchQuery) {
    final ResponseEntity<PagedModelJson<SpendJson>> response = restTemplate.exchange(
        nifflerSpendApiUri + "/v3/spends/all?username={username}&from={from}&to={to}&filterCurrency={filterCurrency}&searchQuery={searchQuery}"
            + new HttpQueryPaginationAndSort(pageable),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<PagedModelJson<SpendJson>>() {
        },
        username,
        filterPeriod != null ? dateFormat(filterDate(filterPeriod)) : null,
        filterPeriod != null ? dateFormat(new Date()) : null,
        filterCurrency != null ? filterCurrency.name() : null,
        searchQuery
    );
    return Optional.ofNullable(
        response.getBody()
    ).orElseThrow(() -> new NoRestResponseException("No REST PagedModel<SpendJson> response is given [/v3/spends/all/ Route]"));
  }

  @Nonnull
  @Override
  public SpendJson addSpend(SpendJson spend) {
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerSpendApiUri + "/spends/add",
            spend,
            SpendJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST SpendJson response is given [/spends/add/ Route]"));
  }

  @Nonnull
  @Override
  public SpendJson editSpend(SpendJson spend) {
    return Optional.ofNullable(
        restTemplate.patchForObject(
            nifflerSpendApiUri + "/spends/edit",
            spend,
            SpendJson.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST SpendJson response is given [/spends/edit/ Route]"));
  }

  @Nonnull
  @Override
  public List<StatisticJson> statistic(String username,
                                       CurrencyValues userCurrency,
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

  @Nonnull
  @Override
  public StatisticV2Json statisticV2(String username,
                                     CurrencyValues statCurrency,
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

  @Override
  public void deleteSpends(String username, List<String> ids) {
    restTemplate.delete(
        nifflerSpendApiUri + "/spends/remove?username={username}&ids={ids}",
        username,
        String.join(",", ids)
    );
  }

  @Override
  public byte[] exportToCsv(String username) {
    final ResponseEntity<byte[]> response = restTemplate.exchange(
        nifflerSpendApiUri + "/v3/spends/export/csv?username={username}",
        HttpMethod.GET,
        null,
        byte[].class,
        username
    );
    return Optional.ofNullable(
        response.getBody()
    ).orElseThrow(() -> new NoRestResponseException("No REST byte[] response is given [/v3/spends/export/csv/ Route]"));
  }
}
