package guru.qa.niffler.api.client;

import guru.qa.niffler.api.service.CategoryService;
import guru.qa.niffler.model.CategoryJson;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Assertions;

public class CategoryRestClient extends BaseRestClient7 {

  public CategoryRestClient() {
    super(CFG.getCategoryUrl());
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
