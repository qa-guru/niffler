package guru.qa.niffler.config;

import guru.qa.niffler.service.cors.CookieCsrfFilter;
import guru.qa.niffler.service.cors.CorsCustomizer;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.net.URI;

import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig {

  private final CorsCustomizer corsCustomizer;
  private final String nifflerAuthUri;

  @Autowired
  public SecurityConfig(CorsCustomizer corsCustomizer,
                        @Value("${niffler-auth.base-uri}") String nifflerAuthUri) {
    this.corsCustomizer = corsCustomizer;
    this.nifflerAuthUri = nifflerAuthUri;
  }

  @Bean
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                        CsrfTokenRepository csrfTokenRepository) throws Exception {
    final String authRpHost = new URI(nifflerAuthUri).getHost();
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
            .permitAll())
        .webAuthn(webauth ->
            webauth.rpName("Niffler Relying Party")
                .rpId(authRpHost)
                .allowedOrigins(corsCustomizer.allowedOrigins()));
    return http.build();
  }

  @Bean
  public CsrfTokenRepository cokieCsrfTokenRepository() {
    return CookieCsrfTokenRepository.withHttpOnlyFalse();
  }
}
