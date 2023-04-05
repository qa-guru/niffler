package niffler.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import niffler.test.gql.model.UpdateUserDataGql;
import niffler.test.gql.model.UserDataGql;
import niffler.test.gql.model.UsersDataGql;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GraphQLApi {

    @POST("/graphql")
    Call<UserDataGql> currentUser(
            @Header("Authorization") String bearerToken,
            @Body JsonNode request);

    @POST("/graphql")
    Call<UpdateUserDataGql> updateUser(
            @Header("Authorization") String bearerToken,
            @Body JsonNode request);

    @POST("/graphql")
    Call<UsersDataGql> allUsers(
            @Header("Authorization") String bearerToken,
            @Body JsonNode request);

    @POST("/graphql")
    Call<UserDataGql> frieds(
            @Header("Authorization") String bearerToken,
            @Body JsonNode request);


}
