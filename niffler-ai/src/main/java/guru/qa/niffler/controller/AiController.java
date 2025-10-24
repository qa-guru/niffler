package guru.qa.niffler.controller;

import guru.qa.niffler.model.ParseSpendingRequest;
import guru.qa.niffler.model.ParseSpendingResponse;
import guru.qa.niffler.service.OllamaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

  private final OllamaService ollamaService;

  @PostMapping("/parse-spending")
  public ResponseEntity<?> parseSpending(@RequestBody ParseSpendingRequest request) {
    try {
      log.info("Received request to parse spending: {}", request.getUserInput());
      
      if (request.getUserInput() == null || request.getUserInput().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("User input cannot be empty");
      }
      
      ParseSpendingResponse response = ollamaService.parseSpending(request.getUserInput());
      
      log.info("Successfully parsed spending: {}", response);
      return ResponseEntity.ok(response);
      
    } catch (Exception e) {
      log.error("Error parsing spending", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to parse spending: " + e.getMessage());
    }
  }

  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("AI service is running");
  }
}

