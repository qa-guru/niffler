package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOHibernate;
import guru.qa.niffler.db.entity.Authority;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.db.jpa.EmfProvider;
import guru.qa.niffler.jupiter.annotation.GenerateUserInAuthDb;
import guru.qa.niffler.model.UserJson;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Arrays;

public class GenerateUserInAuthDataBaseExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver, SuiteExtension {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create(GenerateUserInAuthDataBaseExtension.class);

    private static final NifflerUsersDAO usersDAO = new NifflerUsersDAOHibernate();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateUserInAuthDb annotation = context.getRequiredTestMethod()
                .getAnnotation(GenerateUserInAuthDb.class);

        if (annotation != null) {
            UserEntity dbUserEntity = new UserEntity();
            dbUserEntity.setUsername(annotation.username());
            dbUserEntity.setPassword(annotation.password());
            dbUserEntity.setEnabled(true);
            dbUserEntity.setAccountNonExpired(true);
            dbUserEntity.setAccountNonLocked(true);
            dbUserEntity.setCredentialsNonExpired(true);
            dbUserEntity.setAuthorities(Arrays.stream(Authority.values()).map(
                    a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        ae.setUser(dbUserEntity);
                        return ae;
                    }
            ).toList());
            usersDAO.createUser(dbUserEntity);

            UserJson user = new UserJson();
            user.setUsername(annotation.username());
            user.setPassword(annotation.password());

            context.getStore(NAMESPACE).put("entity", dbUserEntity);
            context.getStore(NAMESPACE).put("user", user);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        UserEntity entity = context.getStore(NAMESPACE).get("entity", UserEntity.class);
        usersDAO.removeUser(entity);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("user", UserJson.class);
    }

    @Override
    public void afterSuite() {
        EmfProvider.INSTANCE.storedEmf()
                .forEach(EntityManagerFactory::close);
    }
}
