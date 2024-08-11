package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.jpa.EmfContext;
import jakarta.persistence.EntityManagerFactory;

public class JpaExtension implements SuiteExtension {

  @Override
  public void afterSuite() {
    EmfContext.storedEmf().forEach(EntityManagerFactory::close);
  }
}
