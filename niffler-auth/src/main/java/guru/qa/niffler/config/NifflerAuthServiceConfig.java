package guru.qa.niffler.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import guru.qa.niffler.config.keys.KeyManager;
import guru.qa.niffler.service.OidcCookiesLogoutAuthenticationSuccessHandler;
import guru.qa.niffler.service.SpecificRequestDumperFilter;
import guru.qa.niffler.service.cors.CorsCustomizer;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Configuration
public class NifflerAuthServiceConfig {

  private final KeyManager keyManager;
  private final String nifflerFrontUri;
  private final String nifflerAuthUri;
  private final String webClientId;
  private final String mobileClientId;
  private final String mobileCustomScheme;
  private final CorsCustomizer corsCustomizer;
  private final String serverPort;
  private final String defaultHttpsPort = "443";
  private final Environment environment;

  @Autowired
  public NifflerAuthServiceConfig(KeyManager keyManager,
                                  @Value("${niffler-front.base-uri}") String nifflerFrontUri,
                                  @Value("${niffler-auth.base-uri}") String nifflerAuthUri,
                                  @Value("${oauth2.web-client-id}") String webClientId,
                                  @Value("${oauth2.mobile-client-id}") String mobileClientId,
                                  @Value("${oauth2.mobile-custom-scheme}") String mobileCustomScheme,
                                  @Value("${server.port}") String serverPort,
                                  CorsCustomizer corsCustomizer,
                                  Environment environment) {
    this.keyManager = keyManager;
    this.nifflerFrontUri = nifflerFrontUri;
    this.nifflerAuthUri = nifflerAuthUri;
    this.webClientId = webClientId;
    this.mobileClientId = mobileClientId;
    this.mobileCustomScheme = mobileCustomScheme;
    this.serverPort = serverPort;
    this.corsCustomizer = corsCustomizer;
    this.environment = environment;
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                    LoginUrlAuthenticationEntryPoint loginEntryPoint) throws Exception {
    OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
        OAuth2AuthorizationServerConfigurer.authorizationServer();

    if (environment.acceptsProfiles(Profiles.of("local", "staging"))) {
      http.addFilterBefore(new SpecificRequestDumperFilter(
          new RequestDumperFilter(),
          "/login", "/connect/logout", "/oauth2/.*"
      ), DisableEncodeUrlFilter.class);
    }

    http
        .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
        .with(authorizationServerConfigurer, authorizationServer ->
            authorizationServer.oidc(oidc ->
                oidc.logoutEndpoint(
                    logout ->
                        logout.logoutResponseHandler(
                            new OidcCookiesLogoutAuthenticationSuccessHandler(
                                new OidcLogoutAuthenticationSuccessHandler(),
                                "JSESSIONID", "XSRF-TOKEN"
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
                loginEntryPoint,
                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            )
        ).sessionManagement(sm -> sm.invalidSessionUrl("/login"));

    corsCustomizer.corsCustomizer(http);
    return http.build();
  }

  @Bean
  @Profile({"staging", "prod"})
  public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPointHttps() {
    LoginUrlAuthenticationEntryPoint entryPoint = loginUrlAuthenticationEntryPointHttp();
    PortMapperImpl portMapper = new PortMapperImpl();
    portMapper.setPortMappings(Map.of(
        serverPort, defaultHttpsPort,
        "80", defaultHttpsPort,
        "8080", "8443"
    ));
    PortResolverImpl portResolver = new PortResolverImpl();
    portResolver.setPortMapper(portMapper);
    entryPoint.setForceHttps(true);
    entryPoint.setPortMapper(portMapper);
    entryPoint.setPortResolver(portResolver);
    return entryPoint;
  }

  @Bean
  @Profile({"local", "docker", "test"})
  public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPointHttp() {
    return new LoginUrlAuthenticationEntryPoint("/login");
  }

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    return new InMemoryRegisteredClientRepository(
        registeredClient(
            webClientId,
            nifflerFrontUri + "/authorized",
            nifflerFrontUri + "/logout"
        ),
        registeredClient(
            mobileClientId,
            mobileCustomScheme + "ru.niffler_android" + "/callback",
            mobileCustomScheme + "ru.niffler_android" + "/logout_callback"
        )
    );
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
  public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
    JWKSet set = new JWKSet(keyManager.rsaKey());
    return (jwkSelector, securityContext) -> jwkSelector.select(set);
  }

  @Bean
  public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
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
            .accessTokenTimeToLive(Duration.of(2, ChronoUnit.HOURS))
            .authorizationCodeTimeToLive(Duration.of(10, ChronoUnit.SECONDS))
            .build())
        .build();
  }
}
