package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.AllPeopleQuery;
import guru.qa.CurrentUserQuery;
import guru.qa.UpdateUserMutation;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.type.UserInput;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Пользователи")
@DisplayName("[GraphQL][niffler-gateway]: Пользователи")
public class GraphQlUsersTest extends BaseGraphQlTest {

  @Test
  @DisplayName("GraphQL: Для нового пользователя должна возвращаться информация из niffler-gateway c дефолтными значениями")
  @AllureId("400001")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      categories = @GenerateCategory(name = "Бар")
  ))
  void currentUserInfoShouldReceived(@User UserJson user,
                                     @Token String bearerToken) throws Exception {
    ApolloCall<CurrentUserQuery.Data> apolloCall = apolloClient.query(new CurrentUserQuery())
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<CurrentUserQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final CurrentUserQuery.Data responseData = response.dataOrThrow();
    final CurrentUserQuery.User userGql = responseData.user;

    step("Check that response contains ID (GUID)", () ->
        assertTrue(userGql.id.matches(ID_REGEXP))
    );
    step("Check that response contains username", () ->
        assertEquals(user.username(), userGql.username)
    );
    step("Check that response contains category", () ->
        assertEquals(user.testData().categories().getFirst().name(), userGql.categories.getFirst().name)
    );
  }

  @Test
  @DisplayName("GraphQL: При обновлении юзера должны сохраняться значения в niffler-userdata")
  @AllureId("400002")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser)
  void updatedUserInfoShouldReceived(@User UserJson user,
                                     @Token String bearerToken) throws Exception {
    ApolloCall<UpdateUserMutation.Data> apolloCall = apolloClient.mutation(new UpdateUserMutation(
            UserInput.builder()
                .fullname("Pizzly Pizzlyvich")
                .build()
        ))
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<UpdateUserMutation.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final UpdateUserMutation.Data responseData = response.dataOrThrow();
    final UpdateUserMutation.User userGql = responseData.user;

    step("Check that response contains ID (GUID)", () ->
        assertTrue(userGql.id.matches(ID_REGEXP))
    );
    step("Check that response contains username", () ->
        assertEquals(user.username(), userGql.username)
    );
    step("Check that response contains updated fullname (Pizzly Pizzlyvich)", () ->
        assertEquals("Pizzly Pizzlyvich", userGql.fullname)
    );
  }

  @Test
  @DisplayName("GraphQL: Список всех пользователей системы не должен быть пустым")
  @AllureId("400003")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser)
  @GenerateUsers({
      @GenerateUser
  })
  void notEmptyUsersListShouldReceived(@Token String bearerToken) throws Exception {
    ApolloCall<AllPeopleQuery.Data> apolloCall = apolloClient.query(new AllPeopleQuery())
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<AllPeopleQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final AllPeopleQuery.Data responseData = response.dataOrThrow();
    final List<AllPeopleQuery.Edge> userGql = responseData.allPeople.edges;
    step("Check that all users list is not empty", () ->
        assertFalse(userGql.isEmpty())
    );
  }
}
