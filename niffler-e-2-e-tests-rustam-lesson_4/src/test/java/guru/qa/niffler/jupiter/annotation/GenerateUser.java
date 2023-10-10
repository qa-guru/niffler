package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.RandomUsersExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(RandomUsersExtension.class)
public @interface GenerateUser {

}
