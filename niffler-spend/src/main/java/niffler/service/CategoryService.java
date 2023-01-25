package niffler.service;

import jakarta.annotation.Nonnull;
import niffler.data.CategoryEntity;
import niffler.data.repository.CategoryRepository;
import niffler.model.CategoryJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public @Nonnull
    List<CategoryJson> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryJson::fromEntity)
                .collect(Collectors.toList());
    }

    public @Nonnull
    CategoryJson addCategory(@Nonnull CategoryJson category) {
        CategoryEntity ce = new CategoryEntity();
        ce.setDescription(category.getDescription());
        return CategoryJson.fromEntity(categoryRepository.save(ce));
    }
}
