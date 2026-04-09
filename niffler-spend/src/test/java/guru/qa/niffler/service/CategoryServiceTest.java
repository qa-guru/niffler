package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.InvalidCategoryNameException;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Test
  void categoryNotFoundExceptionShouldBeThrown(@Mock CategoryRepository categoryRepository) {
    final String username = "not_found";
    final UUID id = UUID.randomUUID();

    Mockito.when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
        .thenReturn(Optional.empty());

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(
        id,
        "",
        username,
        true
    );

    CategoryNotFoundException ex = Assertions.assertThrows(
        CategoryNotFoundException.class,
        () -> categoryService.update(categoryJson)
    );
    Assertions.assertEquals(
        "Can`t find category by id: '" + id + "'",
        ex.getMessage()
    );
  }

  @ValueSource(strings = {"Archived", "ARCHIVED", "ArchIved"})
  @ParameterizedTest
  void categoryNameArchivedShouldBeDenied(String catName, @Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    final UUID id = UUID.randomUUID();
    final CategoryEntity cat = new CategoryEntity();

    Mockito.when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
        .thenReturn(Optional.of(
            cat
        ));

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(
        id,
        catName,
        username,
        true
    );

    InvalidCategoryNameException ex = Assertions.assertThrows(
        InvalidCategoryNameException.class,
        () -> categoryService.update(categoryJson)
    );
    Assertions.assertEquals(
        "Can`t add category with name: '" + catName + "'",
        ex.getMessage()
    );
  }

  @Test
  void onlyTwoFieldsShouldBeUpdated(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    final UUID id = UUID.randomUUID();
    final CategoryEntity cat = new CategoryEntity();
    cat.setId(id);
    cat.setUsername(username);
    cat.setName("Магазины");
    cat.setArchived(false);

    Mockito.when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
        .thenReturn(Optional.of(
            cat
        ));
    Mockito.when(categoryRepository.save(any(CategoryEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(
        id,
        "Бары",
        username,
        true
    );

    categoryService.update(categoryJson);
    ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);
    verify(categoryRepository).save(argumentCaptor.capture());
    assertEquals("Бары", argumentCaptor.getValue().getName());
    assertEquals("duck", argumentCaptor.getValue().getUsername());
    assertTrue(argumentCaptor.getValue().isArchived());
    assertEquals(id, argumentCaptor.getValue().getId());
  }

  @Test
  void addCategoryShouldAlwaysSaveAsNonArchivedRegardlessOfInputFlag(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    Mockito.when(categoryRepository.countByUsernameAndArchived(username, false)).thenReturn(0L);
    Mockito.when(categoryRepository.save(any(CategoryEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    CategoryService categoryService = new CategoryService(categoryRepository);
    CategoryJson inputWithArchivedTrue = new CategoryJson(null, "Транспорт", username, true);

    CategoryJson result = categoryService.addCategory(inputWithArchivedTrue);

    assertFalse(result.archived());
    ArgumentCaptor<CategoryEntity> captor = ArgumentCaptor.forClass(CategoryEntity.class);
    verify(categoryRepository).save(captor.capture());
    assertFalse(captor.getValue().isArchived());
  }

  @ValueSource(strings = {"Archived", "ARCHIVED", "archived"})
  @ParameterizedTest
  void saveShouldThrowInvalidCategoryNameExceptionForArchivedName(String catName,
                                                                   @Mock CategoryRepository categoryRepository) {
    CategoryService categoryService = new CategoryService(categoryRepository);
    CategoryJson categoryJson = new CategoryJson(null, catName, "duck", false);

    assertThrows(
        InvalidCategoryNameException.class,
        () -> categoryService.addCategory(categoryJson)
    );
    verify(categoryRepository, never()).save(any());
  }

  @Test
  void saveShouldThrowTooManyCategoriesExceptionWhenLimitExceeded(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    Mockito.when(categoryRepository.countByUsernameAndArchived(username, false)).thenReturn(8L);

    CategoryService categoryService = new CategoryService(categoryRepository);
    CategoryJson categoryJson = new CategoryJson(null, "Новая категория", username, false);

    TooManyCategoriesException ex = assertThrows(
        TooManyCategoriesException.class,
        () -> categoryService.addCategory(categoryJson)
    );
    assertEquals("Can`t add over than 8 categories for user: '" + username + "'", ex.getMessage());
    verify(categoryRepository, never()).save(any());
  }

  @Test
  void updateShouldThrowTooManyCategoriesExceptionWhenUnarchivingWithFullLimit(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    final UUID id = UUID.randomUUID();
    final CategoryEntity archivedCat = new CategoryEntity();
    archivedCat.setId(id);
    archivedCat.setUsername(username);
    archivedCat.setName("Ресторан");
    archivedCat.setArchived(true);

    Mockito.when(categoryRepository.findByUsernameAndId(username, id))
        .thenReturn(Optional.of(archivedCat));
    Mockito.when(categoryRepository.countByUsernameAndArchived(username, false)).thenReturn(8L);

    CategoryService categoryService = new CategoryService(categoryRepository);
    CategoryJson unarchiveRequest = new CategoryJson(id, "Ресторан", username, false);

    TooManyCategoriesException ex = assertThrows(
        TooManyCategoriesException.class,
        () -> categoryService.update(unarchiveRequest)
    );
    assertEquals("Can`t unarchive category for user: '" + username + "'", ex.getMessage());
  }

  @Test
  void getAllCategoriesWithExcludeArchivedTrueShouldFilterOutArchived(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    CategoryEntity active = new CategoryEntity();
    active.setId(UUID.randomUUID());
    active.setName("Еда");
    active.setUsername(username);
    active.setArchived(false);

    CategoryEntity archived = new CategoryEntity();
    archived.setId(UUID.randomUUID());
    archived.setName("Старая");
    archived.setUsername(username);
    archived.setArchived(true);

    Mockito.when(categoryRepository.findAllByUsernameOrderByName(username))
        .thenReturn(List.of(active, archived));

    CategoryService categoryService = new CategoryService(categoryRepository);
    List<CategoryJson> result = categoryService.getAllCategories(username, true);

    assertEquals(1, result.size());
    assertFalse(result.get(0).archived());
    assertEquals("Еда", result.get(0).name());
  }

  @Test
  void getAllCategoriesWithExcludeArchivedFalseShouldReturnAll(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    CategoryEntity active = new CategoryEntity();
    active.setId(UUID.randomUUID());
    active.setName("Еда");
    active.setUsername(username);
    active.setArchived(false);

    CategoryEntity archived = new CategoryEntity();
    archived.setId(UUID.randomUUID());
    archived.setName("Старая");
    archived.setUsername(username);
    archived.setArchived(true);

    Mockito.when(categoryRepository.findAllByUsernameOrderByName(username))
        .thenReturn(List.of(active, archived));

    CategoryService categoryService = new CategoryService(categoryRepository);
    List<CategoryJson> result = categoryService.getAllCategories(username, false);

    assertEquals(2, result.size());
  }

  @Test
  void getOrSaveShouldReturnExistingCategoryWithoutSaving(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    CategoryEntity existing = new CategoryEntity();
    existing.setId(UUID.randomUUID());
    existing.setName("Кино");
    existing.setUsername(username);

    Mockito.when(categoryRepository.findByUsernameAndName(username, "Кино"))
        .thenReturn(Optional.of(existing));

    CategoryService categoryService = new CategoryService(categoryRepository);
    CategoryJson categoryJson = new CategoryJson(null, "Кино", username, false);

    CategoryEntity result = categoryService.getOrSave(categoryJson);

    assertEquals(existing, result);
    verify(categoryRepository, never()).save(any());
  }

  @Test
  void getOrSaveShouldCreateNewCategoryWhenNotFound(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    CategoryEntity saved = new CategoryEntity();
    saved.setId(UUID.randomUUID());
    saved.setName("Кино");
    saved.setUsername(username);

    Mockito.when(categoryRepository.findByUsernameAndName(username, "Кино"))
        .thenReturn(Optional.empty());
    Mockito.when(categoryRepository.countByUsernameAndArchived(username, false)).thenReturn(0L);
    Mockito.when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(saved);

    CategoryService categoryService = new CategoryService(categoryRepository);
    CategoryJson categoryJson = new CategoryJson(null, "Кино", username, false);

    CategoryEntity result = categoryService.getOrSave(categoryJson);

    assertEquals(saved, result);
    verify(categoryRepository).save(any(CategoryEntity.class));
  }
}