package niffler.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import niffler.api.context.SessionStorageHolder;
import niffler.api.service.RestService;

public class GraphQLClient extends RestService {

    public GraphQLClient() {
        super(CFG.gatewayUrl());
    }

    private final GraphQLApi graphQLApi = retrofit.create(GraphQLApi.class);

    public JsonNode request(JsonNode request) throws Exception {
        return graphQLApi.sendRequest("Bearer " + SessionStorageHolder.getInstance().getToken(), request)
                .execute()
                .body();
    }
}
