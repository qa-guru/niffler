package guru.qa.niffler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseSpendingResponse {
  private double amount;
  private String category;
  private String description;
  private String currency;
  private String spendDate;
}

