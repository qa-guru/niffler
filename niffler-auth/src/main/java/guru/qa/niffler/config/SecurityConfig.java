package guru.qa.niffler.config;

import guru.qa.niffler.service.converter.PublicKeyOptionsConverter;
import guru.qa.niffler.service.cors.CookieCsrfFilter;
import guru.qa.niffler.service.cors.CorsCustomizer;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.webauthn.api.AttestationConveyancePreference;
import org.springframework.security.web.webauthn.api.AuthenticatorAttachment;
import org.springframework.security.web.webauthn.api.AuthenticatorSelectionCriteria;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialCreationOptions;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRpEntity;
import org.springframework.security.web.webauthn.api.ResidentKeyRequirement;
import org.springframework.security.web.webauthn.api.UserVerificationRequirement;
import org.springframework.security.web.webauthn.authentication.PublicKeyCredentialRequestOptionsFilter;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.security.web.webauthn.management.WebAuthnRelyingPartyOperations;
import org.springframework.security.web.webauthn.management.Webauthn4JRelyingPartyOperations;


import java.util.function.Consumer;

import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig {

  private final CorsCustomizer corsCustomizer;
  private final String nifflerAuthRpId;

  @Autowired
  public SecurityConfig(CorsCustomizer corsCustomizer,
                        @Value("${niffler-auth.rp-id}") String nifflerAuthRpId) {
    this.corsCustomizer = corsCustomizer;
    this.nifflerAuthRpId = nifflerAuthRpId;
  }

  @Bean
  @Profile("!docker")
  @ConditionalOnProperty(name = "webauth.enabled", havingValue = "true")
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                        CsrfTokenRepository csrfTokenRepository,
                                                        PublicKeyCredentialRpEntity publicKeyCredentialRpEntity) throws Exception {
    commonSecurityConfiguration(http, csrfTokenRepository)
        .webAuthn(webauth ->
            webauth.rpId(publicKeyCredentialRpEntity.getId())
                .rpName(publicKeyCredentialRpEntity.getName())
                .allowedOrigins(corsCustomizer.allowedOrigins())
                .disableDefaultRegistrationPage(true)
                .messageConverter(new PublicKeyOptionsConverter())
        );
    return http.build();
  }

  @Bean
  @Profile("!docker")
  @ConditionalOnProperty(name = "webauth.enabled", havingValue = "true")
  public PublicKeyCredentialRpEntity publicKeyCredentialRpEntity() {
    return PublicKeyCredentialRpEntity.builder()
        .id(nifflerAuthRpId)
        .name("Niffler Relying Party")
        .build();
  }

  /**
   * Android support
   *
   * @link <a href="https://github.com/kanidm/webauthn-rs/issues/365">gh issue</a>
   * Configuration parameters from demo project
   * @link <a href="https://github.com/DannyMoerkerke/webauthn-demo">demo</a>
   */
  @Bean
  @Profile("!docker")
  @ConditionalOnProperty(name = "webauth.enabled", havingValue = "true")
  public WebAuthnRelyingPartyOperations webAuthnRelyingPartyOperations(PublicKeyCredentialRpEntity publicKeyCredentialRpEntity,
                                                                       PublicKeyCredentialUserEntityRepository jdbcPublicKeyCredentialRepository,
                                                                       UserCredentialRepository jdbcUserCredentialRepository) {
    Webauthn4JRelyingPartyOperations nifflerRelyingParty = new Webauthn4JRelyingPartyOperations(
        jdbcPublicKeyCredentialRepository,
        jdbcUserCredentialRepository,
        publicKeyCredentialRpEntity,
        corsCustomizer.allowedOrigins()
    );
    Consumer<PublicKeyCredentialCreationOptions.PublicKeyCredentialCreationOptionsBuilder> creationCustomizer =
        builder -> builder.authenticatorSelection(
                AuthenticatorSelectionCriteria.builder()
                    .authenticatorAttachment(AuthenticatorAttachment.PLATFORM) // authenticatorAttachment
                    .residentKey(ResidentKeyRequirement.REQUIRED) // rk
                    .userVerification(UserVerificationRequirement.PREFERRED) //uv
                    .build()
            )
            .excludeCredentials(null)
            .attestation(AttestationConveyancePreference.NONE);

    Consumer<PublicKeyCredentialRequestOptions.PublicKeyCredentialRequestOptionsBuilder> requestCustomizer =
        builder -> builder
            .userVerification(UserVerificationRequirement.PREFERRED);

    nifflerRelyingParty.setCustomizeCreationOptions(creationCustomizer);
    nifflerRelyingParty.setCustomizeRequestOptions(requestCustomizer);
    return nifflerRelyingParty;
  }

  /**
   * http supported only for `localhost`
   */
  @Bean
  @Profile("docker")
  @ConditionalOnProperty(name = "webauth.enabled", havingValue = "false")
  public SecurityFilterChain dockerSecurityFilterChain(HttpSecurity http,
                                                       CsrfTokenRepository csrfTokenRepository) throws Exception {
    return commonSecurityConfiguration(http, csrfTokenRepository).build();
  }

  private HttpSecurity commonSecurityConfiguration(HttpSecurity http, CsrfTokenRepository csrfTokenRepository) throws Exception {
    final CsrfTokenRequestAttributeHandler csrfRequestHandler = new CsrfTokenRequestAttributeHandler();
    csrfRequestHandler.setCsrfRequestAttributeName(null);

    corsCustomizer.corsCustomizer(http);
    http.authorizeHttpRequests(customizer -> customizer
            .dispatcherTypeMatchers(DispatcherType.ASYNC, DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
            .requestMatchers(
                "/register",
                "/error",
                "/images/**",
                "/styles/**",
                "/fonts/**",
                "/.well-known/**",
                "/actuator/health"
            ).permitAll()
            .requestMatchers(POST, "/webauthn/authenticate/options").permitAll()
            .requestMatchers(POST, "/login/webauthn").permitAll()
            .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf
            .csrfTokenRepository(csrfTokenRepository)
            // https://stackoverflow.com/a/74521360/65681
            .csrfTokenRequestHandler(csrfRequestHandler)
        )
        .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
        .formLogin(login -> login
            .loginPage("/login")
            .permitAll());
    return http;
  }

  @Bean
  public CsrfTokenRepository cokieCsrfTokenRepository() {
    return CookieCsrfTokenRepository.withHttpOnlyFalse();
  }
}
