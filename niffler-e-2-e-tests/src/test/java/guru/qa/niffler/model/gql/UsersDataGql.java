package guru.qa.niffler.model.gql;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersDataGql extends GqlResponse<UsersDataGql> {
    private List<UserGql> users;
}
