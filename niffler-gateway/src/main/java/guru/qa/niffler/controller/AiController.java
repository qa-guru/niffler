package guru.qa.niffler.controller;

import guru.qa.niffler.config.NifflerGatewayServiceConfig;
import guru.qa.niffler.model.ParseSpendingRequest;
import guru.qa.niffler.model.ParseSpendingResponse;
import guru.qa.niffler.service.AiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@SecurityRequirement(name = NifflerGatewayServiceConfig.OPEN_API_AUTH_SCHEME)
@Tag(name = "AI Controller", description = "AI service integration for parsing spending information")
public class AiController {

  private static final Logger LOG = LoggerFactory.getLogger(AiController.class);

  private final AiClient aiClient;

  @Autowired
  public AiController(AiClient aiClient) {
    this.aiClient = aiClient;
  }

  @PostMapping("/parse-spending")
  @Operation(
      summary = "Parse spending information",
      description = "Parses natural language spending description using AI"
  )
  public ParseSpendingResponse parseSpending(
      @Parameter(description = "User input containing spending information")
      @Valid @RequestBody ParseSpendingRequest request) {
    
    LOG.info("Received request to parse spending through gateway: {}", request.userInput());
    
    ParseSpendingResponse response = aiClient.parseSpending(request);
    
    LOG.info("Successfully parsed spending: {}", response);
    
    return response;
  }
}

