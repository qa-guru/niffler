package guru.qa.niffler.model.graphql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import jakarta.validation.constraints.Size;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UserJsonGQL(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        @Size(max = 30, message = "First name can`t be longer than 30 characters")
        String firstname,
        @JsonProperty("surname")
        @Size(max = 50, message = "Surname can`t be longer than 50 characters")
        String surname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        @Size(max = NifflerGatewayServiceConfig.ONE_MB)
        String photo,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("friendState")
        FriendState friendState,
        @JsonProperty("friends")
        List<UserJsonGQL> friends,
        @JsonProperty("invitations")
        List<UserJsonGQL> invitations) {

    public static @Nonnull UserJsonGQL fromUserJson(@Nonnull UserJson userJson) {
        return new UserJsonGQL(
                userJson.id(),
                userJson.username(),
                userJson.firstname(),
                userJson.surname(),
                userJson.currency(),
                userJson.photo(),
                userJson.friendState(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
