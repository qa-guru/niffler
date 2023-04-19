package niffler.jupiter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import niffler.jupiter.extension.BrowserExtension;
import niffler.jupiter.extension.GenerateSpendExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({BrowserExtension.class, GenerateSpendExtension.class})
public @interface WebTest {

}
