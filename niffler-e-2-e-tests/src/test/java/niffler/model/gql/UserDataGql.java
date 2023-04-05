package niffler.model.gql;

public class UserDataGql extends GqlResponse<UserDataGql> {

    private UserGql user;

    public UserGql getUser() {
        return user;
    }

    public void setUser(UserGql user) {
        this.user = user;
    }
}
