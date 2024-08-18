package guru.qa.niffler.model.rest;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public enum CurrencyValues {
  RUB("₽"), USD("$"), EUR("€"), KZT("₸");
  public final String symbol;

  public static @Nonnull CurrencyValues fromSymbol(@Nonnull String symbol) {
    for (CurrencyValues value : CurrencyValues.values()) {
      if (value.symbol.equals(symbol)) {
        return value;
      }
    }
    throw new IllegalArgumentException("Can`t find CurrencyValues by given symbol: " + symbol);
  }
}
