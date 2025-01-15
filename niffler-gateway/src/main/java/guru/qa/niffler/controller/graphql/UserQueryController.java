package guru.qa.niffler.controller.graphql;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.SelectedField;
import guru.qa.niffler.ex.IllegalGqlFieldAccessException;
import guru.qa.niffler.ex.TooManySubQueriesException;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.gql.UserGql;
import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.api.RestSpendClient;
import guru.qa.niffler.service.utils.GqlQueryPaginationAndSort;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
public class UserQueryController {

  private final UserDataClient userDataClient;
  private final RestSpendClient restSpendClient;

  @Autowired
  public UserQueryController(UserDataClient userDataClient, RestSpendClient restSpendClient) {
    this.userDataClient = userDataClient;
    this.restSpendClient = restSpendClient;
  }

  @SchemaMapping(typeName = "User", field = "friends")
  public Slice<UserGql> friends(UserGql user,
                                @Argument int page,
                                @Argument int size,
                                @Argument @Nullable List<String> sort,
                                @Argument @Nullable String searchQuery) {
    return userDataClient.friends(
        user.username(),
        new GqlQueryPaginationAndSort(page, size, sort).pageable(),
        searchQuery
    ).map(UserGql::fromUserJson);
  }

  @SchemaMapping(typeName = "User", field = "categories")
  public List<CategoryJson> categories(@AuthenticationPrincipal Jwt principal,
                                       UserGql user) {
    final String username = principal.getClaim("sub");
    if (!username.equals(user.username())) {
      throw new IllegalGqlFieldAccessException("Can`t query categories for another user");
    }
    return restSpendClient.getCategories(username, false);
  }

  @QueryMapping
  public Slice<UserGql> allPeople(@AuthenticationPrincipal Jwt principal,
                                  @Argument int page,
                                  @Argument int size,
                                  @Argument @Nullable List<String> sort,
                                  @Argument @Nullable String searchQuery,
                                  @Nonnull DataFetchingEnvironment env) {
    checkSubQueries(env, 2, "friends");
    final String username = principal.getClaim("sub");
    return userDataClient.allUsers(
        username,
        new GqlQueryPaginationAndSort(page, size, sort).pageable(),
        searchQuery
    ).map(UserGql::fromUserJson);
  }

  @QueryMapping
  public UserGql user(@AuthenticationPrincipal Jwt principal,
                      @Nonnull DataFetchingEnvironment env) {
    checkSubQueries(env, 2, "friends");
    final String username = principal.getClaim("sub");
    return UserGql.fromUserJson(
        userDataClient.currentUser(username)
    );
  }

  private void checkSubQueries(@Nonnull DataFetchingEnvironment env, int depth, @Nonnull String... queryKeys) {
    for (String queryKey : queryKeys) {
      List<SelectedField> selectors = env.getSelectionSet().getFieldsGroupedByResultKey().get(queryKey);
      if (selectors != null && selectors.size() > depth) {
        throw new TooManySubQueriesException("Can`t fetch over 2 " + queryKey + " sub-queries");
      }
    }
  }
}
