package guru.qa.niffler.model.gql;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.validation.IsPhotoString;
import jakarta.validation.constraints.Size;

public record UserGqlInput(
    @Size(max = 100, message = "Fullname can`t be longer than 100 characters")
    String fullname,
    @Size(max = NifflerGatewayServiceConfig.ONE_MB)
    @IsPhotoString
    String photo
) {
}
