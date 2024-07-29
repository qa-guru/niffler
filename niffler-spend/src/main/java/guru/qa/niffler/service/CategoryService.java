package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.CategoryNotFoundException;
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

import java.nio.charset.StandardCharsets;
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
    List<CategoryJson> getAllCategories(@Nonnull String username, boolean excludeArchived) {
        return categoryRepository.findAllByUsernameOrderByCategory(username)
                .stream()
                .filter(ce -> !excludeArchived || !ce.isArchived())
                .map(CategoryJson::fromEntity)
                .toList();
    }

    @Transactional
    public @Nonnull
    CategoryJson update(@Nonnull CategoryJson category) {
        CategoryEntity categoryEntity = categoryRepository.findByUsernameAndId(category.username(), category.id())
                .orElseThrow(() -> new CategoryNotFoundException("Can`t find category by id: '" + category.id() + "'"));

        categoryEntity.setCategory(category.category());
        if (!category.archived() && categoryEntity.isArchived()) {
            if (categoryRepository.countByUsernameAndArchived(category.username(), false) > MAX_CATEGORIES_SIZE) {
                LOG.error("### Can`t unarchive category for user: {}", category.username());
                throw new TooManyCategoriesException("Can`t unarchive category for user: '" + category.username() + "'");
            }
        }
        categoryEntity.setArchived(category.archived());
        return CategoryJson.fromEntity(categoryRepository.save(categoryEntity));
    }

    @Transactional
    public @Nonnull
    CategoryJson getOrAddCategory(@Nonnull CategoryJson category) {
        return CategoryJson.fromEntity(getOrSave(category));
    }

    @Transactional
    public @Nonnull
    CategoryJson addCategory(@Nonnull CategoryJson category) {
        return CategoryJson.fromEntity(this.save(category));
    }

    @Nonnull
    @Transactional
    CategoryEntity getOrSave(@Nonnull CategoryJson category) {
        return categoryRepository.findByUsernameAndCategory(category.username(), category.category())
                .orElseGet(() -> this.save(category));
    }

    @Nonnull
    @Transactional
    CategoryEntity save(@Nonnull CategoryJson category) {
        final String username = category.username();
        final String categoryName = category.category();

        if (categoryRepository.countByUsernameAndArchived(username, false) > MAX_CATEGORIES_SIZE) {
            LOG.error("### Can`t add over than 8 categories for user: {}", username);
            throw new TooManyCategoriesException("Can`t add over than 8 categories for user: '" + username + "'");
        }

        CategoryEntity ce = new CategoryEntity();
        ce.setCategory(categoryName);
        ce.setUsername(username);
        ce.setArchived(false);
        try {
            return categoryRepository.save(ce);
        } catch (DataIntegrityViolationException e) {
            LOG.error("### Error while creating category", e);
            throw new NotUniqCategoryException("Category with name '" + categoryName + "' already exists");
        }
    }
}
