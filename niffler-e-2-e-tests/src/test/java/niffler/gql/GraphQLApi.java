package niffler.gql;

import com.fasterxml.jackson.databind.JsonNode;
import niffler.model.gql.UpdateUserDataGql;
import niffler.model.gql.UserDataGql;
import niffler.model.gql.UsersDataGql;
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
