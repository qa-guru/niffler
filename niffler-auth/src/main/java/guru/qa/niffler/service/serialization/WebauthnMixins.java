package guru.qa.niffler.service.serialization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.webauthn.api.AttestationConveyancePreference;
import org.springframework.security.web.webauthn.api.AuthenticationExtensionsClientInputs;
import org.springframework.security.web.webauthn.api.AuthenticatorAttachment;
import org.springframework.security.web.webauthn.api.AuthenticatorSelectionCriteria;
import org.springframework.security.web.webauthn.api.AuthenticatorTransport;
import org.springframework.security.web.webauthn.api.Bytes;
import org.springframework.security.web.webauthn.api.COSEAlgorithmIdentifier;
import org.springframework.security.web.webauthn.api.CredProtectAuthenticationExtensionsClientInput.CredProtect;
import org.springframework.security.web.webauthn.api.CredProtectAuthenticationExtensionsClientInput.CredProtect.ProtectionPolicy;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialDescriptor;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialParameters;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRpEntity;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.api.ResidentKeyRequirement;
import org.springframework.security.web.webauthn.api.UserVerificationRequirement;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Thanks so much for
 * @link <a href="https://github.com/justincranford/spring-security-webauthn-redis">spring-security-webauthn-redis</a>
 */
@SuppressWarnings({"unused"})
public class WebauthnMixins {
  /**
   * @see org.springframework.security.web.webauthn.api.Bytes
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class WebauthnBytesMixIn {
    @JsonCreator
    public WebauthnBytesMixIn(
        @JsonProperty("bytes") byte[] bytes
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class PublicKeyCredentialCreationOptionsMixIn {
    @JsonCreator
    public PublicKeyCredentialCreationOptionsMixIn(
        @JsonProperty("rp") PublicKeyCredentialRpEntity rp,
        @JsonProperty("user") PublicKeyCredentialUserEntity user,
        @JsonProperty("challenge") Bytes challenge,
        @JsonProperty("pubKeyCredParams") List<PublicKeyCredentialParameters> pubKeyCredParams,
        @JsonProperty("timeout") Duration timeout,
        @JsonProperty("excludeCredentials") List<PublicKeyCredentialDescriptor> excludeCredentials,
        @JsonProperty("authenticatorSelection") AuthenticatorSelectionCriteria authenticatorSelection,
        @JsonProperty("attestation") AttestationConveyancePreference attestation,
        @JsonProperty("extensions") AuthenticationExtensionsClientInputs extensions
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.PublicKeyCredentialRpEntity
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class PublicKeyCredentialRpEntityMixIn {
    @JsonCreator
    public PublicKeyCredentialRpEntityMixIn(
        @JsonProperty("name") String name,
        @JsonProperty("id") String id
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class PublicKeyCredentialUserEntityMixIn {
    @JsonCreator
    public PublicKeyCredentialUserEntityMixIn(
        @JsonProperty("name") String name,
        @JsonProperty("id") Bytes id,
        @JsonProperty("displayName") String displayName
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.PublicKeyCredentialParameters
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class PublicKeyCredentialParametersMixIn {
    @JsonCreator
    public PublicKeyCredentialParametersMixIn(
        @JsonProperty("type") PublicKeyCredentialType type,
        @JsonProperty("alg") COSEAlgorithmIdentifier alg
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.AuthenticatorSelectionCriteria
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class AuthenticatorSelectionCriteriaMixIn {
    @JsonCreator
    public AuthenticatorSelectionCriteriaMixIn(
        @JsonProperty("authenticatorAttachment") AuthenticatorAttachment authenticatorAttachment,
        @JsonProperty("residentKey") ResidentKeyRequirement residentKey,
        @JsonProperty("userVerification") UserVerificationRequirement userVerification
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.AttestationConveyancePreference
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class AttestationConveyancePreferenceMixIn {
    @JsonCreator
    public AttestationConveyancePreferenceMixIn(
        @JsonProperty("value") String value
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.ResidentKeyRequirement
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class ResidentKeyRequirementMixIn {
    @JsonCreator
    public ResidentKeyRequirementMixIn(
        @JsonProperty("value") String value
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.UserVerificationRequirement
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class UserVerificationRequirementMixIn {
    @JsonCreator
    public UserVerificationRequirementMixIn(
        @JsonProperty("value") String value
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class PublicKeyCredentialRequestOptionsMixIn {
    @JsonCreator
    public PublicKeyCredentialRequestOptionsMixIn(
        @JsonProperty("challenge") Bytes challenge,
        @JsonProperty("timeout") Duration timeout,
        @JsonProperty("rpId") String rpId,
        @JsonProperty("allowCredentials") List<PublicKeyCredentialDescriptor> allowCredentials,
        @JsonProperty("userVerification") UserVerificationRequirement userVerification,
        @JsonProperty("extensions") AuthenticationExtensionsClientInputs extensions
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.AuthenticatorAttachment
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class AuthenticatorAttachmentMixIn {
    @JsonCreator
    public AuthenticatorAttachmentMixIn(
        @JsonProperty("value") String value
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.AuthenticationExtensionsClientInputs
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class AuthenticationExtensionsClientInputsMixIn {
    @JsonCreator
    public AuthenticationExtensionsClientInputsMixIn(
        @JsonProperty("inputs") List<?> inputs
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.AuthenticationExtensionsClientInput
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class AuthenticationExtensionsClientInputMixIn {
    @JsonCreator
    public AuthenticationExtensionsClientInputMixIn(
        @JsonProperty("extensionId") String extensionId,
        @JsonProperty("input") String input
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.PublicKeyCredentialDescriptor
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class PublicKeyCredentialDescriptorMixIn {
    @JsonCreator
    public PublicKeyCredentialDescriptorMixIn(
        @JsonProperty("type") PublicKeyCredentialType type,
        @JsonProperty("id") Bytes id,
        @JsonProperty("transports") Set<AuthenticatorTransport> transports
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.PublicKeyCredentialType
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class PublicKeyCredentialTypeMixIn {
    @JsonCreator
    public PublicKeyCredentialTypeMixIn(
        @JsonProperty("value") String value
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.COSEAlgorithmIdentifier
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class COSEAlgorithmIdentifierMixIn {
    @JsonCreator
    public COSEAlgorithmIdentifierMixIn(
        @JsonProperty("value") long value
    ) {
    }
  }


  /**
   * @see org.springframework.security.web.webauthn.api.AuthenticatorTransport
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class AuthenticatorTransportMixIn {
    @JsonCreator
    public AuthenticatorTransportMixIn(
        @JsonProperty("value") String value
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.api.CredProtectAuthenticationExtensionsClientInput
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class CredProtectAuthenticationExtensionsClientInputMixIn {
    @JsonCreator
    public CredProtectAuthenticationExtensionsClientInputMixIn(
        @JsonProperty("input") CredProtect input
    ) {
    }

    @JsonIgnore
    public abstract String getExtensionId();
  }

  /**
   * @see org.springframework.security.web.webauthn.api.CredProtectAuthenticationExtensionsClientInput.CredProtect
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class CredProtectMixIn {
    @JsonCreator
    public CredProtectMixIn(
        @JsonProperty("credProtectionPolicy") ProtectionPolicy credProtectionPolicy,
        @JsonProperty("enforceCredentialProtectionPolicy") boolean enforceCredentialProtectionPolicy
    ) {
    }
  }

  /**
   * @see org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
  public static abstract class WebAuthnAuthenticationMixIn {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    abstract boolean isAuthenticated();

    @JsonIgnore
    abstract void setAuthenticated(boolean authenticated);

    @JsonCreator
    public WebAuthnAuthenticationMixIn(
        @JsonProperty("principal") PublicKeyCredentialUserEntity principal,
        @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities
    ) {
    }
  }
}
