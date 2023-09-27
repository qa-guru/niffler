package guru.qa.niffler.service.cors;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Component
public class CorsCustomizer {

    private final String nifflerFrontUri;
    private final String nifflerAuthUri;

    @Autowired
    public CorsCustomizer(@Value("${niffler-front.base-uri}") String nifflerFrontUri,
                          @Value("${niffler-auth.base-uri}") String nifflerAuthUri) {
        this.nifflerFrontUri = nifflerFrontUri;
        this.nifflerAuthUri = nifflerAuthUri;
    }

    public void corsCustomizer(@Nonnull HttpSecurity http) throws Exception {
        http.cors(c -> {
            CorsConfigurationSource source = s -> {
                CorsConfiguration cc = new CorsConfiguration();
                cc.setAllowCredentials(true);
                cc.setAllowedOrigins(List.of(nifflerFrontUri, nifflerAuthUri));
                cc.setAllowedHeaders(List.of("*"));
                cc.setAllowedMethods(List.of("*"));
                return cc;
            };

            c.configurationSource(source);
        });
    }
}
