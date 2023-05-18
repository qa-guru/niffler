package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ClasspathUserConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.params.converter.ConvertWith;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ConvertWith(ClasspathUserConverter.class)
public @interface ClasspathUser {

}
