package guru.qa.niffler.config;

import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.soap.SoapUserDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NifflerGatewayServiceConfig {

    public static final int ONE_MB = 1024 * 1024;

    private final String nifflerUserdataBaseUri;

    @Autowired
    public NifflerGatewayServiceConfig(@Value("${niffler-userdata.base-uri}") String nifflerUserdataBaseUri) {
        this.nifflerUserdataBaseUri = nifflerUserdataBaseUri;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("guru.qa.niffler.userdata.wsdl");
        return marshaller;
    }

    @Bean
    @ConditionalOnProperty(prefix = "niffler-userdata", name = "client", havingValue = "soap")
    public UserDataClient userDataClient(Jaxb2Marshaller marshaller) {
        SoapUserDataClient client = new SoapUserDataClient();
        client.setDefaultUri(nifflerUserdataBaseUri + "/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
