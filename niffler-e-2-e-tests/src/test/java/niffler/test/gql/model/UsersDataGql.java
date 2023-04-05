package niffler.test.gql.model;

import java.util.List;

public class UsersDataGql extends GqlResponse<UsersDataGql> {

    private List<UserGql> users;

    public List<UserGql> getUsers() {
        return users;
    }

    public void setUsers(List<UserGql> users) {
        this.users = users;
    }
}
