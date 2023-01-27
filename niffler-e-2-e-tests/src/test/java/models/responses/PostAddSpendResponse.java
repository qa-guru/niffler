package models.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostAddSpendResponse {

    @NonNull
    private String spendDate;

    @NonNull
    private String category;

    @NonNull
    private String currency;

    @NonNull
    private Integer amount;

    @NonNull
    private String description;

    @NonNull
    private String username;
}
