package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

@RestTest
public abstract class BaseRestTest {

    protected static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @RegisterExtension
    private static ApiLoginExtension apiLoginExtension = ApiLoginExtension.create(false);
}
