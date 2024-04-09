package guru.qa.niffler.model.gql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataGql extends GqlResponse<UserDataGql> {
    private UserGql user;
}
