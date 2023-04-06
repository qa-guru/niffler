package niffler.model.gql;

public class UpdateUserDataGql extends GqlResponse<UpdateUserDataGql> {

    private UserGql updateUser;

    public UserGql getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UserGql updateUser) {
        this.updateUser = updateUser;
    }
}
