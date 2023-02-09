package niffler.jupiter;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import niffler.api.NifflerSpendClient;
import niffler.model.SpendJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BeforeCheckSpendTable implements BeforeAllCallback, AfterAllCallback {

    private final NifflerSpendClient nsc = new NifflerSpendClient();
    public static final List<String> requestList = Arrays.asList("data/spend0.json");
    private static final List<String> listSpendJson = new ArrayList<>();
    private final static String username = "dima";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        for (String s: requestList) {
            URL url = Resources.getResource(s);
            String text = Resources.toString(url, UTF_8);
            SpendJson spend = new Gson().fromJson(text, SpendJson.class);
            SpendJson created = nsc.createSpend(spend);
            Assertions.assertNotNull(created.getId());
            listSpendJson.add(created.getId().toString());
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        nsc.deleteSpend(username, listSpendJson);
    }
}
