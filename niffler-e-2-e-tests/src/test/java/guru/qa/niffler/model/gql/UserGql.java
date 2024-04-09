package guru.qa.niffler.model.gql;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.FriendState;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserGql {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("photoSmall")
    private String photoSmall;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("friendState")
    private FriendState friendState;
    @JsonProperty("friends")
    private List<UserGql> friends;
    @JsonProperty("invitations")
    private List<UserGql> invitations;
    @JsonProperty("__typename")
    private String typeName;
}
