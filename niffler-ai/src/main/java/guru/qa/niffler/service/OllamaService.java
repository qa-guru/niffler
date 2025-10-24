package guru.qa.niffler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.OllamaRequest;
import guru.qa.niffler.model.OllamaResponse;
import guru.qa.niffler.model.ParseSpendingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class OllamaService {

  @Value("${ollama.api.url}")
  private String ollamaApiUrl;

  @Value("${ollama.api.token}")
  private String ollamaApiToken;

  @Value("${ollama.api.model}")
  private String ollamaModel;

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public ParseSpendingResponse parseSpending(String userInput) throws Exception {
    String prompt = createPrompt(userInput);
    
    OllamaRequest request = new OllamaRequest(ollamaModel, prompt, false);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(ollamaApiToken);
    
    HttpEntity<OllamaRequest> entity = new HttpEntity<>(request, headers);
    
    log.info("Sending request to Ollama API: {}", ollamaApiUrl);
    OllamaResponse response = restTemplate.postForObject(ollamaApiUrl, entity, OllamaResponse.class);
    
    if (response == null || response.getResponse() == null) {
      throw new RuntimeException("Invalid response from Ollama API");
    }
    
    log.info("Received response from Ollama: {}", response.getResponse());
    
    return parseAiResponse(response.getResponse());
  }

  private String createPrompt(String userInput) {
    String todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
    return String.format(
        "You are a helpful assistant that extracts spending information from natural language. " +
        "Extract the following information from the user's input and return ONLY a valid JSON object (no markdown, no code blocks, no explanations):\n" +
        "- amount (number)\n" +
        "- category (string, one of: \"Обучение\", \"Отдых\", \"Рестораны\", \"Продукты\", \"Транспорт\", \"Спорт\", or if doesn't match any of these, use the closest match)\n" +
        "- description (string, a brief description)\n" +
        "- currency (string, one of: \"RUB\", \"USD\", \"EUR\", \"KZT\". Default to \"RUB\" if not specified)\n" +
        "- spendDate (string, ISO date format. Use today's date if not specified: %s)\n\n" +
        "User input: \"%s\"\n\n" +
        "Return only JSON in this exact format:\n" +
        "{\"amount\": 0, \"category\": \"string\", \"description\": \"string\", \"currency\": \"string\", \"spendDate\": \"string\"}",
        todayDate, userInput
    );
  }

  private ParseSpendingResponse parseAiResponse(String aiResponse) throws Exception {
    // Remove markdown code blocks if present
    String cleanResponse = aiResponse
        .replaceAll("```json\\s*", "")
        .replaceAll("```\\s*", "")
        .trim();
    
    log.info("Cleaned AI response: {}", cleanResponse);
    
    try {
      return objectMapper.readValue(cleanResponse, ParseSpendingResponse.class);
    } catch (Exception e) {
      log.error("Failed to parse AI response: {}", cleanResponse, e);
      throw new RuntimeException("Failed to parse spending information from AI response: " + e.getMessage());
    }
  }
}

