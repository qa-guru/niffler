package guru.qa.niffler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import guru.qa.niffler.config.keys.KeyManager;
import guru.qa.niffler.service.OidcCookiesLogoutAuthenticationSuccessHandler;
import guru.qa.niffler.service.cors.CorsCustomizer;
import guru.qa.niffler.service.serialization.JdbcSessionObjectMapperConfigurer;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.webauthn.management.JdbcPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Configuration
public class NifflerAuthServiceConfig implements BeanClassLoaderAware {

  private final KeyManager keyManager;
  private final String nifflerFrontUri;
  private final String nifflerAuthUri;
  private final String androidAppUri;
  private final String webClientId;
  private final String mobileClientId;
  private final String mobileCustomScheme;
  private final CorsCustomizer corsCustomizer;
  private final Environment environment;

  /**
   * For ObjectMapper configuration
   *
   * @see guru.qa.niffler.config.NifflerAuthServiceConfig#jdbcOAuth2AuthorizationService(JdbcOperations, RegisteredClientRepository, ObjectMapper)
   */
  private ClassLoader classLoader;

  @Autowired
  public NifflerAuthServiceConfig(KeyManager keyManager,
                                  @Value("${niffler-front.base-uri}") String nifflerFrontUri,
                                  @Value("${niffler-auth.base-uri}") String nifflerAuthUri,
                                  @Value("${oauth2.android-app-uri}") String androidAppUri,
                                  @Value("${oauth2.web-client-id}") String webClientId,
                                  @Value("${oauth2.mobile-client-id}") String mobileClientId,
                                  @Value("${oauth2.mobile-custom-scheme}") String mobileCustomScheme,
                                  CorsCustomizer corsCustomizer,
                                  Environment environment) {
    this.keyManager = keyManager;
    this.nifflerFrontUri = nifflerFrontUri;
    this.nifflerAuthUri = nifflerAuthUri;
    this.androidAppUri = androidAppUri;
    this.webClientId = webClientId;
    this.mobileClientId = mobileClientId;
    this.mobileCustomScheme = mobileCustomScheme;
    this.corsCustomizer = corsCustomizer;
    this.environment = environment;
  }

  @Override
  public void setBeanClassLoader(@Nonnull ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        OAuth2AuthorizationServerConfigurer.authorizationServer();

    http
        .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
        .with(authorizationServerConfigurer, authorizationServer ->
            authorizationServer.oidc(oidc ->
                oidc.logoutEndpoint(
                    logout ->
                        logout.logoutResponseHandler(
                            new OidcCookiesLogoutAuthenticationSuccessHandler(
                                new OidcLogoutAuthenticationSuccessHandler(),
                                "XSRF-TOKEN"
                            )
                        )
                )
            )
        )
        .authorizeHttpRequests(authorize ->
            authorize.anyRequest().authenticated()
        )
        .exceptionHandling(ex -> ex
            .accessDeniedPage("/error")
            .defaultAuthenticationEntryPointFor(
                new LoginUrlAuthenticationEntryPoint("/login"),
                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            )
        )
        .sessionManagement(sm -> sm.invalidSessionUrl("/login"));

    corsCustomizer.corsCustomizer(http);
    if (environment.matchesProfiles("prod | staging")) {
      http.redirectToHttps(Customizer.withDefaults());
    }

    return http.build();
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
    RegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcOperations);
    RegisteredClient webClient = registeredClientRepository.findByClientId(webClientId);
    RegisteredClient mobileClient = registeredClientRepository.findByClientId(mobileClientId);
    if (webClient == null) {
      registeredClientRepository.save(
          registeredClient(
              webClientId,
              nifflerFrontUri + Callbacks.Web.login,
              nifflerFrontUri + Callbacks.Web.logout
          )
      );
    }
    if (mobileClient == null) {
      registeredClientRepository.save(
          registeredClient(
              mobileClientId,
              mobileCustomScheme + androidAppUri + Callbacks.Android.login,
              mobileCustomScheme + androidAppUri + Callbacks.Android.logout
          )
      );
    }
    return registeredClientRepository;
  }

  /**
   * Do not use @Primary ObjectMapper directly!
   * Only configured via JdbcSessionObjectMapperConfigure:
   *
   * @see JdbcSessionObjectMapperConfigurer
   */
  @Bean
  public OAuth2AuthorizationService jdbcOAuth2AuthorizationService(JdbcOperations jdbc,
                                                                   RegisteredClientRepository registeredClientRepository,
                                                                   ObjectMapper objectMapper) {
    final ObjectMapper configuredOm = JdbcSessionObjectMapperConfigurer.apply(this.classLoader, objectMapper);
    var jdbcOAuth2AuthorizationService = new JdbcOAuth2AuthorizationService(
        jdbc,
        registeredClientRepository
    );
    final var oAuth2AuthorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
    final var oAuth2AuthorizationParametersMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationParametersMapper();
    oAuth2AuthorizationRowMapper.setObjectMapper(configuredOm);
    oAuth2AuthorizationParametersMapper.setObjectMapper(configuredOm);
    jdbcOAuth2AuthorizationService.setAuthorizationRowMapper(oAuth2AuthorizationRowMapper);
    jdbcOAuth2AuthorizationService.setAuthorizationParametersMapper(oAuth2AuthorizationParametersMapper);
    return jdbcOAuth2AuthorizationService;
  }

  @Bean
  public OAuth2AuthorizationConsentService authorizationConsentService(JdbcOperations jdbc,
                                                                       RegisteredClientRepository registeredClientRepository) {
    return new JdbcOAuth2AuthorizationConsentService(jdbc, registeredClientRepository);
  }

  @Bean
  public PublicKeyCredentialUserEntityRepository jdbcPublicKeyCredentialRepository(JdbcOperations jdbc) {
    return new JdbcPublicKeyCredentialUserEntityRepository(jdbc);
  }

  @Bean
  public UserCredentialRepository jdbcUserCredentialRepository(JdbcOperations jdbc) {
    return new JdbcUserCredentialRepository(jdbc);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder()
        .issuer(nifflerAuthUri)
        .build();
  }

  @Bean
  public DefaultCookieSerializerCustomizer defaultCookieSerializerCustomizer() {
    return cookieSerializer -> cookieSerializer.setUseBase64Encoding(false);
  }

  @Bean
  public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
    JWKSet set = new JWKSet(keyManager.rsaKey());
    return (jwkSelector, securityContext) -> jwkSelector.select(set);
  }

  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }

  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
    return context -> {
      if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
        context.getClaims().expiresAt(Instant.now().plus(60, ChronoUnit.MINUTES));
      }
    };
  }

  private RegisteredClient registeredClient(String clientId, String redirectUri, String logoutRedirectUri) {
    return RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId(clientId)
        .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .redirectUri(redirectUri)
        .scope(OidcScopes.OPENID)
        .scope(OidcScopes.PROFILE)
        .clientSettings(ClientSettings.builder()
            .requireAuthorizationConsent(true)
            .requireProofKey(true)
            .build()
        )
        .postLogoutRedirectUri(logoutRedirectUri)
        .tokenSettings(TokenSettings.builder()
            .accessTokenTimeToLive(Duration.of(60, ChronoUnit.MINUTES))
            .authorizationCodeTimeToLive(Duration.of(10, ChronoUnit.SECONDS))
            .build())
        .build();
  }
}
