package guru.qa.niffler.config;

import guru.qa.niffler.service.cors.CookieCsrfFilter;
import guru.qa.niffler.service.cors.CorsCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final CorsCustomizer corsCustomizer;

    @Autowired
    public SecurityConfig(CorsCustomizer corsCustomizer) {
        this.corsCustomizer = corsCustomizer;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.corsCustomizer(http);

        http.authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/register", "/images/**", "/styles/**", "/fonts/**", "/actuator/health")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // https://stackoverflow.com/a/74521360/65681
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())
                .logout(logout ->
                        logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // https://github.com/spring-projects/spring-authorization-server/issues/266
                                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
                )
                .sessionManagement()
                .invalidSessionUrl("/login");

        return http.formLogin().and().build();
    }
}
