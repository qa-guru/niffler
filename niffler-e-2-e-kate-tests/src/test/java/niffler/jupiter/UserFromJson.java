package niffler.jupiter;

import org.junit.jupiter.params.converter.ConvertWith;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ConvertWith(UserFromJsonConverter.class)
public @interface UserFromJson {
}
