package niffler.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJson {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID id;
    private String username;
    private String firstname;
    private String surname;
    private Currency currency;
    private String photo;
}
