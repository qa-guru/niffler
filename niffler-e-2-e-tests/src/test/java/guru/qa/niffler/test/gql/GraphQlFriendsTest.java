package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.apollo.api.Error;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.Friends2SubQueriesQuery;
import guru.qa.FriendsQuery;
import guru.qa.FriendsWithCategoriesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.IncomeInvitations;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static guru.qa.niffler.model.rest.FriendshipStatus.INVITE_RECEIVED;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("[GraphQL][niffler-gateway]: Друзья")
@DisplayName("[GraphQL][niffler-gateway]: Друзья")
public class GraphQlFriendsTest extends BaseGraphQlTest {

  @Test
  @DisplayName("GraphQL: Для нового пользователя должен возвращаться пустой список friends из niffler-gateway")
  @AllureId("400004")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser)
  void emptyFriendsAndInvitationsListShouldReceivedForNewUser(@Token String bearerToken) throws Exception {
    ApolloCall<FriendsQuery.Data> apolloCall = apolloClient.query(new FriendsQuery())
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<FriendsQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final FriendsQuery.Data responseData = response.dataOrThrow();

    final List<FriendsQuery.Edge> friends = responseData.user.friends.edges;

    step("Check that friends list is empty", () ->
        assertTrue(friends.isEmpty())
    );
  }

  @CsvSource({
      "friends"
  })
  @ParameterizedTest(name = "Получена ошибка Can`t fetch over 2 {0} sub-queries")
  @DisplayName("GraphQL: Невозможно получить более 2-х уровней вложенности запросов")
  @AllureId("400005")
  @Tag("GraphQL")
  @ApiLogin(
      user = @GenerateUser(
          friends = @Friends(count = 2),
          incomeInvitations = @IncomeInvitations(count = 2)
      )
  )
  void errorShouldReceivedForOver2SubQueries(String expectedMessagePart,
                                             @Token String bearerToken) throws Exception {
    ApolloCall<Friends2SubQueriesQuery.Data> apolloCall = apolloClient.query(new Friends2SubQueriesQuery())
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<Friends2SubQueriesQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();

    final Error firstError = response.errors.getFirst();

    assertNull(response.data);
    assertEquals(
        "Can`t fetch over 2 " + expectedMessagePart + " sub-queries",
        firstError.getMessage()
    );
  }

  @Test
  @DisplayName("GraphQL: Для пользователя должен возвращаться список друзей " +
      "и исходящих запросов на дружбу из niffler-userdata")
  @AllureId("400006")
  @Tag("GraphQL")
  @ApiLogin(
      user = @GenerateUser(
          friends = @Friends(count = 1),
          incomeInvitations = @IncomeInvitations(count = 1)
      )
  )
  void friendsAndIncomeInvitationsListShouldReceived(@User UserJson user,
                                                     @Token String bearerToken) throws Exception {
    UserJson friend = user.testData().friends().getFirst();
    UserJson invitation = user.testData().incomeInvitations().getFirst();

    ApolloCall<FriendsQuery.Data> apolloCall = apolloClient.query(new FriendsQuery())
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<FriendsQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final FriendsQuery.Data responseData = response.dataOrThrow();

    final List<FriendsQuery.Edge> friends = responseData.user.friends.edges;

    step("Check friend in response", () -> {
      assertEquals(2, friends.size());
      step("Check sorting by status", () ->
          assertEquals(INVITE_RECEIVED.name(), friends.getFirst().node.friendshipStatus.rawValue)
      );

      final FriendsQuery.Node friendUser = friends.getLast().node;

      assertEquals(friend.id().toString(), friendUser.id);
      assertEquals(friend.username(), friendUser.username);
      assertEquals(FriendshipStatus.FRIEND.name(), friendUser.friendshipStatus.rawValue);
    });
    step("Check income invitation in response", () -> {
      final FriendsQuery.Node invitationUser = friends.getFirst().node;

      assertEquals(invitation.id().toString(), invitationUser.id);
      assertEquals(invitation.username(), invitationUser.username);
      assertEquals(FriendshipStatus.INVITE_RECEIVED.name(), invitationUser.friendshipStatus.rawValue);
    });
  }

  @Test
  @DisplayName("GraphQL: Невозможно получить чужие категории")
  @AllureId("400007")
  @Tag("GraphQL")
  @ApiLogin(
      user = @GenerateUser(
          categories = @GenerateCategory("Бар"),
          friends = @Friends(count = 1, categories = @GenerateCategory("Магазин"))
      )
  )
  void errorShouldReceivedForOtherPeopleCategories(@Token String bearerToken) throws Exception {
    ApolloCall<FriendsWithCategoriesQuery.Data> apolloCall = apolloClient.query(new FriendsWithCategoriesQuery())
        .addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<FriendsWithCategoriesQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();

    final Error firstError = response.errors.getFirst();

    assertNotNull(response.data); //because exception in @SchemaMapping, not @QueryMapping
    assertEquals(
        "Can`t query categories for another user",
        firstError.getMessage()
    );
  }
}
