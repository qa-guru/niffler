package guru.qa.niffler.config;

import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.api.RestUserDataClient;
import guru.qa.niffler.service.soap.SoapUserDataClient;
import guru.qa.niffler.service.utils.GqlVoidScalar;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Configuration
public class NifflerGatewayServiceConfig {

  public static final int ONE_MB = 1024 * 1024;
  public static final String OPEN_API_AUTH_SCHEME = "bearer";

  private final String nifflerUserdataBaseUri;
  private final String nifflerGatewayBaseUri;
  private final String userdataClient;

  @Autowired
  public NifflerGatewayServiceConfig(@Value("${niffler-userdata.base-uri}") String nifflerUserdataBaseUri,
                                     @Value("${niffler-gateway.base-uri}") String nifflerGatewayBaseUri,
                                     @Value("${niffler-userdata.client}") String userdataClient) {
    this.nifflerUserdataBaseUri = nifflerUserdataBaseUri;
    this.nifflerGatewayBaseUri = nifflerGatewayBaseUri;
    this.userdataClient = userdataClient;
  }

  @Bean
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("jaxb.userdata");
    return marshaller;
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public UserDataClient userDataClient(Jaxb2Marshaller marshaller,
                                       RestTemplate restTemplate) {
    if ("soap".equals(userdataClient)) {
      SoapUserDataClient client = new SoapUserDataClient();
      client.setDefaultUri(nifflerUserdataBaseUri + "/ws");
      client.setMarshaller(marshaller);
      client.setUnmarshaller(marshaller);
      return client;
    } else {
      return new RestUserDataClient(
          restTemplate,
          nifflerUserdataBaseUri
      );
    }
  }

  @Bean
  public OpenAPI openAPI() {
    Server server = new Server();
    server.setUrl(nifflerGatewayBaseUri);
    return new OpenAPI()
        .servers(List.of(server))
        .info(new Info()
            .title("Niffler Gateway API Documentation")
            .version("1.0")
            .description("API documentation with Swagger and SpringDoc"))
        .addSecurityItem(new SecurityRequirement().addList(OPEN_API_AUTH_SCHEME))
        .components(new Components()
            .addSecuritySchemes(OPEN_API_AUTH_SCHEME, new SecurityScheme()
                .name(OPEN_API_AUTH_SCHEME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
  }

  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    return wiringBuilder -> wiringBuilder.scalar(GqlVoidScalar.Void);
  }
}
