package guru.qa.niffler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.service.cors.CookieCsrfFilter;
import guru.qa.niffler.service.cors.CorsCustomizer;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

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
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                        CsrfTokenRepository csrfTokenRepository) throws Exception {
    commonSecurityConfiguration(http, csrfTokenRepository)
        .webAuthn(webauth ->
            webauth.rpName("Niffler Relying Party")
                .rpId(nifflerAuthRpId)
                .allowedOrigins(corsCustomizer.allowedOrigins()));
    return http.build();
  }

  /**
   * http supported only for `localhost`
   */
  @Bean
  @Profile("docker")
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
