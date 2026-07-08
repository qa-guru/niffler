package guru.qa.niffler.model.rest;

import lombok.RequiredArgsConstructor;
import javax.annotation.ParametersAreNonnullByDefault;

@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public enum DataFilterValues {
  TODAY("Today"), WEEK("last week"), MONTH("Last month");
  public final String text;
}
