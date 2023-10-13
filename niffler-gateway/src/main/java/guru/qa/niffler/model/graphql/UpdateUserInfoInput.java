package guru.qa.niffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.CurrencyValues;


public record UpdateUserInfoInput(
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        String photo) {

}
