package guru.qa.niffler.service;

import guru.qa.niffler.model.ParseSpendingRequest;
import guru.qa.niffler.model.ParseSpendingResponse;
import jakarta.annotation.Nonnull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AiClient {

  @Nonnull
  ParseSpendingResponse parseSpending(ParseSpendingRequest request);
}

