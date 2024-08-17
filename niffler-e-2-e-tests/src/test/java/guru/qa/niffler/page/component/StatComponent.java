package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$;

public class StatComponent extends BaseComponent<StatComponent> {
  public StatComponent() {
    super($("#stat"));
  }

  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
}
