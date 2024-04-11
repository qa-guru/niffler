package guru.qa.niffler.test.soap;

import guru.qa.niffler.api.UserdataWsClient;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;

@SoapTest
public abstract class BaseSoapTest {

    protected static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    protected static final UserdataWsClient wsClient = new UserdataWsClient();
}
