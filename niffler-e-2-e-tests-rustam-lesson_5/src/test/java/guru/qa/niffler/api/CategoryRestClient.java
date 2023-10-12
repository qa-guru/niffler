package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;

public class CategoryRestClient extends BaseRestClient {

  public CategoryRestClient() {
    super(Config.getInstance().getCategoryUrl());
  }

  private final CategoryService categoryService = retrofit.create(CategoryService.class);

  public @Nonnull CategoryJson addCategory(CategoryJson category) {
    try {
      return categoryService.addCategory(category).execute().body();
    } catch (IOException e) {
      Assertions.fail("Can`t execute api call to niffler-spend: " + e.getMessage());
      return null;
    }
  }
}
