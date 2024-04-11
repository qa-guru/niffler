package guru.qa.niffler.model.gql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDataGql extends GqlResponse<UpdateUserDataGql> {
    private UserGql updateUser;
}
