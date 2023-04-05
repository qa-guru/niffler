package niffler.test.gql.model;

public class UpdateUserDataGql extends GqlResponse<UpdateUserDataGql> {

    private UserGql updateUser;

    public UserGql getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UserGql updateUser) {
        this.updateUser = updateUser;
    }
}
