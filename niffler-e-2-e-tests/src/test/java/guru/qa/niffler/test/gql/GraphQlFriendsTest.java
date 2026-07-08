package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.apollo.api.Error;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.Friends2SubQueriesQuery;
import guru.qa.FriendshipAcceptMutation;
import guru.qa.FriendshipAddMutation;
import guru.qa.FriendshipDeleteMutation;
import guru.qa.FriendshipRejectMutation;
import guru.qa.FriendsQuery;
import guru.qa.FriendsWithCategoriesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Friends;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.GenerateUsers;
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
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[GraphQL][niffler-gateway]: Друзья")
@DisplayName("[GraphQL][niffler-gateway]: Друзья")
@ParametersAreNonnullByDefault
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
          categories = @GenerateCategory(name = "Бар"),
          friends = @Friends(count = 1, categories = @GenerateCategory(name = "Магазин"))
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

  @Test
  @AllureId("400017")
  @DisplayName("GraphQL: Mutation FriendshipAdd отправляет запрос на дружбу")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser)
  @GenerateUsers({
      @GenerateUser
  })
  void friendshipAddMutationTest(@User(selector = User.Selector.METHOD) UserJson target,
                                 @Token String bearerToken) throws Exception {
    ApolloCall<FriendshipAddMutation.Data> apolloCall = apolloClient.mutation(
        new FriendshipAddMutation(target.username())
    ).addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<FriendshipAddMutation.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final FriendshipAddMutation.Friendship result = response.dataOrThrow().friendship;

    step("Check that returned user has correct username", () ->
        assertEquals(target.username(), result.username)
    );
    step("Check that friendship status is INVITE_SENT", () ->
        assertEquals(FriendshipStatus.INVITE_SENT.name(), result.friendshipStatus.rawValue)
    );
  }

  @Test
  @AllureId("400018")
  @DisplayName("GraphQL: Mutation FriendshipAccept принимает входящий запрос на дружбу")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      incomeInvitations = @IncomeInvitations(count = 1)
  ))
  void friendshipAcceptMutationTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final UserJson inviter = user.testData().incomeInvitations().getFirst();

    ApolloCall<FriendshipAcceptMutation.Data> apolloCall = apolloClient.mutation(
        new FriendshipAcceptMutation(inviter.username())
    ).addHttpHeader("Authorization", bearerToken);

    final FriendshipAcceptMutation.Friendship result =
        Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow().friendship;

    step("Check that inviter username matches", () ->
        assertEquals(inviter.username(), result.username)
    );
    step("Check that friendship status is FRIEND", () ->
        assertEquals(FriendshipStatus.FRIEND.name(), result.friendshipStatus.rawValue)
    );
  }

  @Test
  @AllureId("400019")
  @DisplayName("GraphQL: Mutation FriendshipReject отклоняет входящий запрос на дружбу")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      incomeInvitations = @IncomeInvitations(count = 1)
  ))
  void friendshipRejectMutationTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final UserJson inviter = user.testData().incomeInvitations().getFirst();

    ApolloCall<FriendshipRejectMutation.Data> apolloCall = apolloClient.mutation(
        new FriendshipRejectMutation(inviter.username())
    ).addHttpHeader("Authorization", bearerToken);

    final FriendshipRejectMutation.Friendship result =
        Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow().friendship;

    step("Check that inviter username matches", () ->
        assertEquals(inviter.username(), result.username)
    );
    step("Check that friendship status is empty after reject", () ->
        assertNull(result.friendshipStatus)
    );
  }

  @Test
  @AllureId("400020")
  @DisplayName("GraphQL: Mutation FriendshipDelete удаляет друга")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser(
      friends = @Friends(count = 1)
  ))
  void friendshipDeleteMutationTest(@User UserJson user, @Token String bearerToken) throws Exception {
    final UserJson friend = user.testData().friends().getFirst();

    ApolloCall<FriendshipDeleteMutation.Data> apolloCall = apolloClient.mutation(
        new FriendshipDeleteMutation(friend.username())
    ).addHttpHeader("Authorization", bearerToken);

    Rx2Apollo.single(apolloCall).blockingGet().dataOrThrow();

    ApolloCall<FriendsQuery.Data> friendsCall = apolloClient.query(new FriendsQuery())
        .addHttpHeader("Authorization", bearerToken);

    final List<FriendsQuery.Edge> friendsAfter =
        Rx2Apollo.single(friendsCall).blockingGet().dataOrThrow().user.friends.edges;

    step("Check that friends list is empty after deletion", () ->
        assertTrue(friendsAfter.isEmpty())
    );
  }
}
