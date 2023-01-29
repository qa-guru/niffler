package model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("username")
    private String username;

}
