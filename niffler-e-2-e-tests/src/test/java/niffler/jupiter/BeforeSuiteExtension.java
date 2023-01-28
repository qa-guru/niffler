package niffler.jupiter;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.restassured.config.DecoderConfig.decoderConfig;

public class BeforeSuiteExtension implements AroundAllTestsExtension {

    @Override
    public void beforeAllTests(ExtensionContext context) {
        RestAssured.config = RestAssured.config().decoderConfig(decoderConfig().defaultContentCharset(StandardCharsets.UTF_8));
        RestAssured.filters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));

        System.out.println("BEFORE SUITE");
    }

    @Override
    public void afterAllTests() {
        System.out.println("AFTER SUITE!");
    }
}
