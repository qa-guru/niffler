package niffler.jupiter.annotation.meta;

import io.qameta.allure.junit5.AllureJunit5;
import niffler.jupiter.extension.BrowserExtension;
import niffler.jupiter.extension.ClearCookiesAndSessionExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({BrowserExtension.class, ClearCookiesAndSessionExtension.class, AllureJunit5.class})
public @interface WebTest {

}
