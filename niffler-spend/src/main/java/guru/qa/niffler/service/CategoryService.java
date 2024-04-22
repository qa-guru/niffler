package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.NotUniqCategoryException;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.CategoryJson;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    private static final int MAX_CATEGORIES_SIZE = 7;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public @Nonnull
    List<CategoryJson> getAllCategories(@Nonnull String username) {
        return categoryRepository.findAllByUsernameOrderByCategory(username)
                .stream()
                .map(CategoryJson::fromEntity)
                .toList();
    }

    @Transactional
    public @Nonnull
    CategoryJson addCategory(@Nonnull CategoryJson category) {
        final String username = category.username();
        final String categoryName = category.category();

        if (categoryRepository.countByUsername(username) > MAX_CATEGORIES_SIZE) {
            LOG.error("### Can`t add over than 8 categories for user: {}", username);
            throw new TooManyCategoriesException("Can`t add over than 8 categories for user: '" + username + "'");
        }

        CategoryEntity ce = new CategoryEntity();
        ce.setCategory(categoryName);
        ce.setUsername(username);
        try {
            return CategoryJson.fromEntity(categoryRepository.save(ce));
        } catch (DataIntegrityViolationException e) {
            LOG.error("### Error while creating category", e);
            throw new NotUniqCategoryException("Category with name '" + categoryName + "' already exists");
        }
    }
}
