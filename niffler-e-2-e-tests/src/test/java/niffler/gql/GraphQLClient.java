package niffler.gql;

import com.fasterxml.jackson.databind.JsonNode;
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

    public UserDataGql currentUser(JsonNode request) throws Exception {
        return graphQLApi.currentUser("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }

    public UpdateUserDataGql updateUser(JsonNode request) throws Exception {
        return graphQLApi.updateUser("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }

    public UsersDataGql allUsers(JsonNode request) throws Exception {
        return graphQLApi.allUsers("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }

    public UserDataGql friends(JsonNode request) throws Exception {
        return graphQLApi.frieds("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }
}
