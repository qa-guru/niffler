package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

public class SpendRestClient extends BaseRestClient {

    public SpendRestClient() {
        super(CFG.getSpendUrl());
    }

    private final SpendService spendService = retrofit.create(SpendService.class);

    public @Nonnull SpendJson addSpend(SpendJson spend) {
        try {
            return Objects.requireNonNull(spendService.addSpend(spend).execute().body());
        } catch (IOException e) {
            Assertions.fail("Can`t execute api call to niffler-spend: " + e.getMessage());
            return null;
        }
    }
}
