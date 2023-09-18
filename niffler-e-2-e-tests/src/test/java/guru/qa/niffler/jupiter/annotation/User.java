package guru.qa.niffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static guru.qa.niffler.jupiter.extension.AbstractCreateUserExtension.API_LOGIN_USERS_NAMESPACE;
import static guru.qa.niffler.jupiter.extension.AbstractCreateUserExtension.ON_METHOD_USERS_NAMESPACE;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {

    UserType userType() default UserType.COMMON;

    Selector selector() default Selector.NESTED;

    @Deprecated // for study only, see UsersQueueExtension
    enum UserType {
        ADMIN, COMMON
    }

    enum Selector {
        METHOD, NESTED;

        public ExtensionContext.Namespace getNamespace() {
            switch (this) {
                case METHOD -> {
                    return ON_METHOD_USERS_NAMESPACE;
                }
                case NESTED -> {
                    return API_LOGIN_USERS_NAMESPACE;
                }
                default -> throw new IllegalStateException();
            }
        }
    }
}
