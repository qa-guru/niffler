package guru.qa.niffler.service.api;

import guru.qa.niffler.ex.NoRestResponseException;
import guru.qa.niffler.model.ParseSpendingRequest;
import guru.qa.niffler.model.ParseSpendingResponse;
import guru.qa.niffler.service.AiClient;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@Component
@ParametersAreNonnullByDefault
public class RestAiClient implements AiClient {

  private static final Logger LOG = LoggerFactory.getLogger(RestAiClient.class);

  private final RestTemplate restTemplate;
  private final String nifflerAiApiUri;

  @Autowired
  public RestAiClient(RestTemplate restTemplate,
                      @Value("${niffler-ai.base-uri}") String nifflerAiBaseUri) {
    this.restTemplate = restTemplate;
    this.nifflerAiApiUri = nifflerAiBaseUri + "/api/ai";
  }

  @Nonnull
  @Override
  public ParseSpendingResponse parseSpending(ParseSpendingRequest request) {
    LOG.info("Forwarding request to AI service: {}", request);
    
    return Optional.ofNullable(
        restTemplate.postForObject(
            nifflerAiApiUri + "/parse-spending",
            request,
            ParseSpendingResponse.class
        )
    ).orElseThrow(() -> new NoRestResponseException("No REST ParseSpendingResponse response is given [/api/ai/parse-spending Route]"));
  }
}

