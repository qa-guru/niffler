package guru.qa.niffler.jupiter;

import org.junit.jupiter.api.extension.ExtensionContext;

public class BeforeSuiteExtension implements AroundAllTestsExtension {

  @Override
  public void beforeAllTests(ExtensionContext context) {
    System.out.println("### BEFORE SUITE!!!");
  }

  @Override
  public void afterAllTests() {
    System.out.println("### AFTER SUITE!!!");
  }
}
