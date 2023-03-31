package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String userName;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private Currency currency;
    @JsonProperty("photo")
    private byte[] photo;

    public UserJson() {
    }
}

