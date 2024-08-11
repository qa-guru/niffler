package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.IssueExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(IssueExtension.class)
public @interface DisabledByIssue {
  String value();
}
