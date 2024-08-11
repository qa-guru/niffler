package guru.qa.niffler.data;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CurrencyValues {
  RUB("Rub-cropped.svg"), USD("Dollar-cropped.svg"), EUR("Euro-cropped.svg"), KZT("Tenge-cropped.svg");

  public final String symbolResource;
}
