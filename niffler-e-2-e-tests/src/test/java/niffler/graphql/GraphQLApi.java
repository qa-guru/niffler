package niffler.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GraphQLApi {

    @POST("/graphql")
    Call<JsonNode> sendRequest(
            @Header("Authorization") String bearerToken,
            @Body JsonNode request);
}
