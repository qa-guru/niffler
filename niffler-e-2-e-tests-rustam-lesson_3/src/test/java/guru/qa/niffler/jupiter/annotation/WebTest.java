package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(BrowserExtension.class)
public @interface WebTest {
}
