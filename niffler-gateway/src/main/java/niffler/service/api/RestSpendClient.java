package niffler.service.api;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import niffler.model.CategoryJson;
import niffler.model.CurrencyValues;
import niffler.model.DataFilterValues;
import niffler.model.SpendJson;
import niffler.model.StatisticJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class RestSpendClient {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private final WebClient webClient;
    private final String nifflerSpendUri;

    @Autowired
    public RestSpendClient(WebClient webClient,
                           @Value("${niffler-spend.base-uri}") String nifflerSpendUri) {
        this.webClient = webClient;
        this.nifflerSpendUri = nifflerSpendUri;
    }

    public List<CategoryJson> getCategories() {
        return webClient.get()
                .uri(nifflerSpendUri + "/categories")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CategoryJson>>() {
                })
                .block();
    }

    public @Nonnull
    List<SpendJson> getSpends(@Nonnull String username,
                              @Nullable DataFilterValues filterPeriod,
                              @Nullable CurrencyValues filterCurrency) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        Optional.ofNullable(filterPeriod).ifPresent(f -> {
            params.add("from", dateFormat(filterDate(filterPeriod)));
            params.add("to", dateFormat(new Date()));
        });
        Optional.ofNullable(filterCurrency).ifPresent(dfv -> params.add("filterCurrency", filterCurrency.name()));
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerSpendUri + "/spends").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SpendJson>>() {
                })
                .block();
    }

    public @Nonnull
    SpendJson addSpend(@Nonnull SpendJson spend) {
        return webClient.post()
                .uri(nifflerSpendUri + "/addSpend")
                .body(Mono.just(spend), SpendJson.class)
                .retrieve()
                .bodyToMono(SpendJson.class)
                .block();
    }

    public @Nonnull
    List<StatisticJson> statistic(@Nonnull String username,
                                  @Nonnull CurrencyValues userCurrency,
                                  @Nullable CurrencyValues filterCurrency,
                                  @Nullable DataFilterValues filterPeriod) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("userCurrency", userCurrency.name());
        Optional.ofNullable(filterCurrency).ifPresent(c -> params.add("filterCurrency", c.name()));
        Optional.ofNullable(filterPeriod).ifPresent(f -> {
            params.add("from", dateFormat(filterDate(filterPeriod)));
            params.add("to", dateFormat(new Date()));
        });
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerSpendUri + "/statistic").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StatisticJson>>() {
                })
                .block();
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
