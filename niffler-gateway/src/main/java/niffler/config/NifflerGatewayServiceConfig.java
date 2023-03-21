package niffler.config;

import niffler.service.ws.SoapUserDataClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NifflerGatewayServiceConfig {

    public static final int THREE_MB = 3145728;

    private final String nifflerUserdataBaseUri;

    @Autowired
    public NifflerGatewayServiceConfig(@Value("${niffler-userdata.base-uri}") String nifflerUserdataBaseUri) {
        this.nifflerUserdataBaseUri = nifflerUserdataBaseUri;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(
                        configurer -> configurer.defaultCodecs().maxInMemorySize(THREE_MB)).build())
                .build();
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("niffler.userdata.wsdl");
        return marshaller;
    }

    @Bean
    public SoapUserDataClient userDataClient(Jaxb2Marshaller marshaller) {
        SoapUserDataClient client = new SoapUserDataClient();
        client.setDefaultUri(nifflerUserdataBaseUri + "/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
