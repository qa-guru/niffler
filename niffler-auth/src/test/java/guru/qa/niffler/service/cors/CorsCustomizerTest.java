package guru.qa.niffler.service.cors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CorsCustomizerTest {

    private final CorsCustomizer corsCustomizer = new CorsCustomizer(
            "front-uri",
            "auth-uri"
    );

    @Test
    void corsCustomizerTest(@Mock HttpSecurity httpSecurity) throws Exception {
        corsCustomizer.corsCustomizer(httpSecurity);
        verify(httpSecurity, times(1)).cors(any(Customizer.class));
    }

    @Test
    void corsConfigurationSourceTest() {
        CorsConfigurationSource configurationSource = corsCustomizer.corsConfigurationSource();
        final CorsConfiguration configuration = configurationSource.getCorsConfiguration(null);

        Assertions.assertTrue(configuration.getAllowCredentials());
        Assertions.assertEquals(
                List.of("front-uri", "auth-uri"),
                configuration.getAllowedOrigins()
        );
        Assertions.assertEquals(
                List.of("*"),
                configuration.getAllowedHeaders()
        );
        Assertions.assertEquals(
                List.of("*"),
                configuration.getAllowedMethods()
        );
    }

    @Test
    void customizerTest(@Mock CorsConfigurer<HttpSecurity> c) {
        final Customizer<CorsConfigurer<HttpSecurity>> customizer = corsCustomizer.customizer();

        customizer.customize(c);

        verify(c, times(1))
                .configurationSource(any(CorsConfigurationSource.class));
    }
}
