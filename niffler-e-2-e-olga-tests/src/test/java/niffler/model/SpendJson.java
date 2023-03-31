package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
public class SpendJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("spendDate")
    private Date spendDate;
    @JsonProperty("category")
    private String category;
    @JsonProperty("currency")
    private Currency currency;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("username")
    private String username;
}
