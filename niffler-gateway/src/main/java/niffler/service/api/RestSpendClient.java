package niffler.service.api;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import niffler.model.CategoryJson;
import niffler.model.CurrencyJson;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class RestSpendClient {

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

    public List<SpendJson> getSpends(String username, @Nullable DataFilterValues filter, @Nullable CurrencyValues currency) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        Optional.ofNullable(filter).ifPresent(dfv -> params.add("filter", filter.name()));
        Optional.ofNullable(currency).ifPresent(dfv -> params.add("currency", currency.name()));
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerSpendUri + "/spends").queryParams(params).build().toUri();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<SpendJson>>() {
                })
                .block();
    }

    public SpendJson addSpend(SpendJson spend) {
        return webClient.post()
                .uri(nifflerSpendUri + "/addSpend")
                .body(Mono.just(spend), SpendJson.class)
                .retrieve()
                .bodyToMono(SpendJson.class)
                .block();
    }

    public List<StatisticJson> statistic(@Nonnull String username,
                                         @Nonnull List<CurrencyJson> currencyRates,
                                         @Nonnull CurrencyValues userCurrency,
                                         @Nullable CurrencyValues filterCurrency,
                                         @Nullable Date from,
                                         @Nullable Date to) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", username);
        params.add("userCurrency", userCurrency.name());

        Optional.ofNullable(filterCurrency).ifPresent(c -> params.add("filterCurrency", c.name()));
        Optional.ofNullable(from).ifPresent(f -> params.add("from", f.toString()));
        Optional.ofNullable(to).ifPresent(t -> params.add("to", t.toString()));
        URI uri = UriComponentsBuilder.fromHttpUrl(nifflerSpendUri + "/statistic").queryParams(params).build().toUri();

        return webClient.post()
                .uri(uri)
                .body(Mono.just(currencyRates), new ParameterizedTypeReference<List<CurrencyJson>>() {
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StatisticJson>>() {
                })
                .block();
    }
}
