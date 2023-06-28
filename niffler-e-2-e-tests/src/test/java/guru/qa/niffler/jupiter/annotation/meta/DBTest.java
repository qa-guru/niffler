package guru.qa.niffler.jupiter.annotation.meta;

import guru.qa.niffler.jupiter.extension.DAOResolver;
import guru.qa.niffler.jupiter.extension.JpaExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({JpaExtension.class, DAOResolver.class})
public @interface DBTest {

}
