package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SpendDto {

    @JsonProperty("spendDate")
    private String spendDate;
    @JsonProperty("category")
    private String category;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("username")
    private String username;

}
