package guru.qa.niffler.condition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
  yellow("rgba(255, 183, 3, 1)"),
  green("rgba(53, 173, 123, 1)"),
  blue100("rgba(41, 65, 204, 1)"),
  orange("rgba(251, 133, 0, 1)"),
  azure("rgba(33, 158, 188, 1)"),
  blue200("rgba(22, 41, 149, 1)"),
  red("rgba(247, 89, 67, 1)"),
  skyBlue("rgba(99, 181, 226, 1)"),
  purple("rgba(148, 85, 198, 1)");

  public final String rgb;
}
