package guru.qa.niffler.gql;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.context.SessionStorageHolder;
import guru.qa.niffler.api.service.RestService;
import guru.qa.niffler.model.gql.UpdateUserDataGql;
import guru.qa.niffler.model.gql.UserDataGql;
import guru.qa.niffler.model.gql.UsersDataGql;
import io.qameta.allure.Step;

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
