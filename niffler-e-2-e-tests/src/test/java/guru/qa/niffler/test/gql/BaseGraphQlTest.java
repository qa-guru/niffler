package guru.qa.niffler.test.gql;

import guru.qa.niffler.api.GatewayGqlApiClient;
import guru.qa.niffler.jupiter.annotation.meta.GqlTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.test.rest.BaseRestTest;
import org.junit.jupiter.api.extension.RegisterExtension;

@GqlTest
public abstract class BaseGraphQlTest extends BaseRestTest {

    @RegisterExtension
    private static ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    protected static final GatewayGqlApiClient gqlClient = new GatewayGqlApiClient();
}
