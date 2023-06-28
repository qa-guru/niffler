package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.model.CategoryJson;
import jakarta.annotation.Nonnull;
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
    private static final int MAX_CATEGORIES_SIZE = 7;
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
        final String username = category.getUsername();
        final String categoryName = category.getCategory();

        if (categoryRepository.findAllByUsername(username).size() > MAX_CATEGORIES_SIZE) {
            LOG.error("### Can`t add over than 7 categories for user: " + username);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "Can`t add over than 7 categories for user: '" + username);
        }

        CategoryEntity ce = new CategoryEntity();
        ce.setCategory(categoryName);
        ce.setUsername(username);
        try {
            return CategoryJson.fromEntity(categoryRepository.save(ce));
        } catch (DataIntegrityViolationException e) {
            LOG.error("### Error while creating category: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Category with name '" + categoryName + "' already exists", e);
        }
    }
}
