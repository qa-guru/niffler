package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.GetSessionQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.annotation.ParametersAreNonnullByDefault;

@Epic("[GraphQL][niffler-gateway]: Session")
@DisplayName("[GraphQL][niffler-gateway]: Session")
@ParametersAreNonnullByDefault
public class GraphQlSessionTest extends BaseGraphQlTest {

  @Test
  @AllureId("400021")
  @DisplayName("GraphQL: Query getSession возвращает данные текущей сессии")
  @Tag("GraphQL")
  @ApiLogin(user = @GenerateUser)
  void getSessionQueryTest(@User UserJson user, @Token String bearerToken) throws Exception {
    ApolloCall<GetSessionQuery.Data> apolloCall = apolloClient.query(
        new GetSessionQuery()
    ).addHttpHeader("Authorization", bearerToken);

    final ApolloResponse<GetSessionQuery.Data> response = Rx2Apollo.single(apolloCall).blockingGet();
    final GetSessionQuery.Session session = response.dataOrThrow().session;

    step("Check session is not null", () ->
        assertNotNull(session)
    );
    step("Check username matches", () ->
        assertEquals(user.username(), session.username)
    );
    step("Check issuedAt is not null", () ->
        assertNotNull(session.issuedAt)
    );
    step("Check expiresAt is not null", () ->
        assertNotNull(session.expiresAt)
    );
  }
}
