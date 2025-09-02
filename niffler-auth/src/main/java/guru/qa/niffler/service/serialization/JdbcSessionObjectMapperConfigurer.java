package guru.qa.niffler.service.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.webauthn.api.AuthenticationExtensionsClientInput;
import org.springframework.security.web.webauthn.api.AuthenticationExtensionsClientInputs;
import org.springframework.security.web.webauthn.api.AuthenticatorSelectionCriteria;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.CredProtectAuthenticationExtensionsClientInput;
import org.springframework.security.web.webauthn.api.ImmutableAuthenticationExtensionsClientInputs;
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialDescriptor;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialParameters;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRpEntity;
import org.springframework.security.web.webauthn.api.UserVerificationRequirement;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;
import org.springframework.security.web.webauthn.jackson.WebauthnJackson2Module;
import org.springframework.web.servlet.FlashMap;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This configuration should be used in following beans:
 * @see guru.qa.niffler.config.SessionConfig#springSessionConversionService(ObjectMapper)
 * @see guru.qa.niffler.config.NifflerAuthServiceConfig#jdbcOAuth2AuthorizationService(JdbcOperations, RegisteredClientRepository, ObjectMapper)
 * Thanks so much for
 * @link <a href="https://stackoverflow.com/questions/79281677/how-to-use-spring-security-6-4-0-passkeys-with-redishttpsessionrepository/79299083#79299083">stackoverflow</a>
 * @link <a href="https://github.com/spring-projects/spring-security/issues/16328">github issue</a>
 *
 * Also, important to use @Primary ObjectMapper from ApplicationContext as parameter & bean classloader
 */
public class JdbcSessionObjectMapperConfigurer {

  public static ObjectMapper apply(ClassLoader classLoader, ObjectMapper source) {
    ObjectMapper copy = source.copy();
    copy.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
        .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
        .configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true)
        .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true)
        .configure(DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, true)
        .configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false)
        .configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

    copy.registerModules(new WebauthnJackson2Module());
    copy.registerModules(SecurityJackson2Modules.getModules(classLoader));
    copy.registerModules(new OAuth2AuthorizationServerJackson2Module());

    copy.addMixIn(PublicKeyCredentialCreationOptions.class, WebauthnMixins.PublicKeyCredentialCreationOptionsMixIn.class);
    copy.addMixIn(ImmutablePublicKeyCredentialUserEntity.class, WebauthnMixins.PublicKeyCredentialUserEntityMixIn.class);
    copy.addMixIn(PublicKeyCredentialRpEntity.class, WebauthnMixins.PublicKeyCredentialRpEntityMixIn.class);
    copy.addMixIn(PublicKeyCredentialParameters.class, WebauthnMixins.PublicKeyCredentialParametersMixIn.class);
    copy.addMixIn(AuthenticatorSelectionCriteria.class, WebauthnMixins.AuthenticatorSelectionCriteriaMixIn.class);
    copy.addMixIn(PublicKeyCredentialRequestOptions.class, WebauthnMixins.PublicKeyCredentialRequestOptionsMixIn.class);
    copy.addMixIn(ImmutableAuthenticationExtensionsClientInputs.class, WebauthnMixins.AuthenticationExtensionsClientInputsMixIn.class);
    copy.addMixIn(AuthenticationExtensionsClientInputs.class, WebauthnMixins.AuthenticationExtensionsClientInputsMixIn.class);
    copy.addMixIn(AuthenticationExtensionsClientInput.class, WebauthnMixins.AuthenticationExtensionsClientInputMixIn.class);
    copy.addMixIn(PublicKeyCredentialDescriptor.class, WebauthnMixins.PublicKeyCredentialDescriptorMixIn.class);
    copy.addMixIn(CredProtectAuthenticationExtensionsClientInput.class, WebauthnMixins.CredProtectAuthenticationExtensionsClientInputMixIn.class);
    copy.addMixIn(CredProtectAuthenticationExtensionsClientInput.CredProtect.class, WebauthnMixins.CredProtectMixIn.class);
    copy.addMixIn(UserVerificationRequirement.class, WebauthnMixins.UserVerificationRequirementMixIn.class);
    copy.addMixIn(WebAuthnAuthentication.class, WebauthnMixins.WebAuthnAuthenticationMixIn.class);
    copy.addMixIn(Bytes.class, WebauthnMixins.WebauthnBytesMixIn.class);

    copy.addMixIn(CopyOnWriteArrayList.class, CommonMixins.CopyOnWriteArrayListMixin.class);
    copy.addMixIn(FlashMap.class, CommonMixins.FlashMapMixin.class);
    copy.addMixIn(HashSet.class, CommonMixins.HashSetMixin.class);
    return copy;
  }
}
