package niffler.service;

import jakarta.annotation.Nonnull;
import niffler.data.CategoryEntity;
import niffler.data.repository.CategoryRepository;
import niffler.model.CategoryJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public @Nonnull
    List<CategoryJson> getAllCategories(@Nonnull String username) {
        return categoryRepository.findAllByUsername(username)
                .stream()
                .map(CategoryJson::fromEntity)
                .collect(Collectors.toList());
    }

    public @Nonnull
    CategoryJson addCategory(@Nonnull CategoryJson category) {
        if (categoryRepository.findAllByUsername(category.getUsername()).size() > 7) {
            LOG.error("### Can`t add over than 7 categories for user: " + category.getUsername());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Can`t add over than 7 categories for user: '" + category.getUsername());
        }

        CategoryEntity ce = new CategoryEntity();
        ce.setDescription(category.getDescription());
        ce.setUsername(category.getUsername());
        try {
            return CategoryJson.fromEntity(categoryRepository.save(ce));
        } catch (DataIntegrityViolationException e) {
            LOG.error("### Error while creating category: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Category with name '" + category.getDescription() + "' already exists", e);
        }
    }
}
