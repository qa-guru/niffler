package niffler.service;

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

    public List<CategoryJson> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryJson::fromEntity)
                .collect(Collectors.toList());
    }
}
