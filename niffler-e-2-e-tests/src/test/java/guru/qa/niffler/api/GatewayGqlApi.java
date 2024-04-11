package guru.qa.niffler.api;

import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.UpdateUserDataGql;
import guru.qa.niffler.model.gql.UserDataGql;
import guru.qa.niffler.model.gql.UsersDataGql;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GatewayGqlApi {

    @POST("graphql")
    Call<UserDataGql> currentUser(
            @Header("Authorization") String bearerToken,
            @Body GqlRequest request);

    @POST("graphql")
    Call<UpdateUserDataGql> updateUser(
            @Header("Authorization") String bearerToken,
            @Body GqlRequest request);

    @POST("graphql")
    Call<UsersDataGql> allUsers(
            @Header("Authorization") String bearerToken,
            @Body GqlRequest request);

    @POST("graphql")
    Call<UserDataGql> friends(
            @Header("Authorization") String bearerToken,
            @Body GqlRequest request);

}
