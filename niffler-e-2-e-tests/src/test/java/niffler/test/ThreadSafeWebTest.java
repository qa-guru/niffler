package niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import niffler.config.Config;
import niffler.config.LocalConfig;
import niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;

public class ThreadSafeWebTest {

    static {
        Configuration.browserSize = "1980x1920";
    }

    private static final Config CFG = new LocalConfig();

    private MainPage mainPage = new MainPage();

    static Stream<Arguments> checkFilterTest() {
        return Stream.of(
                Arguments.of("Last week", new String[] {"Коктейль"}),
                Arguments.of("Last month", new String[] {"Коктейль", "Просто бар!", "Описание"})
        );
    }

    @MethodSource
    @ParameterizedTest
    void checkFilterTest(String button, String[] expectedTexts) {
        Allure.step("Check login", () -> {
            Selenide.open(CFG.frontUrl());
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue("dima");
            $("input[name='password']").setValue("12345");
            $("button[type='submit']").click();
            $(".header__title").shouldBe(Condition.visible)
                    .shouldHave(Condition.text("Niffler. The coin keeper."));
        });

        mainPage.clickByButton(button)
                .checkTableContains(expectedTexts);
    }
}
