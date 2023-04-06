package niffler.gql;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import niffler.api.context.SessionStorageHolder;
import niffler.api.service.RestService;
import niffler.model.gql.UpdateUserDataGql;
import niffler.model.gql.UserDataGql;
import niffler.model.gql.UsersDataGql;

public class GraphQLClient extends RestService {

    public GraphQLClient() {
        super(CFG.gatewayUrl());
    }

    private final GraphQLApi graphQLApi = retrofit.create(GraphQLApi.class);

    @Step("Send SOAP POST('/graphql') request to niffler-gateway, query: CurrentUser")
    public UserDataGql currentUser(JsonNode request) throws Exception {
        return graphQLApi.currentUser("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/graphql') request to niffler-gateway, mutation: UpdateUser")
    public UpdateUserDataGql updateUser(JsonNode request) throws Exception {
        return graphQLApi.updateUser("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/graphql') request to niffler-gateway, query: Users")
    public UsersDataGql allUsers(JsonNode request) throws Exception {
        return graphQLApi.allUsers("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }

    @Step("Send SOAP POST('/graphql') request to niffler-gateway, query: Friends")
    public UserDataGql friends(JsonNode request) throws Exception {
        return graphQLApi.frieds("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }
}
