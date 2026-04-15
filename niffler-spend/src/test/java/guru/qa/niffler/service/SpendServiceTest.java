package guru.qa.niffler.service;

import com.opencsv.CSVReader;
import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.ex.SpendNotFoundException;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpendServiceTest {

  @Mock
  private SpendRepository spendRepository;

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private SpendService spendService;

  private CategoryEntity categoryEntity;
  private CategoryJson categoryJson;
  private SpendEntity spendEntity;

  @BeforeEach
  void setUp() {
    categoryEntity = new CategoryEntity();
    categoryEntity.setId(UUID.randomUUID());
    categoryEntity.setName("Еда");
    categoryEntity.setUsername("duck");
    categoryEntity.setArchived(false);

    categoryJson = new CategoryJson(categoryEntity.getId(), "Еда", "duck", false);

    spendEntity = new SpendEntity();
    spendEntity.setId(UUID.randomUUID());
    spendEntity.setUsername("duck");
    spendEntity.setAmount(500.0);
    spendEntity.setDescription("Обед");
    spendEntity.setCurrency(CurrencyValues.RUB);
    spendEntity.setSpendDate(new Date());
    spendEntity.setCategory(categoryEntity);
  }

  @Test
  void saveSpendForUserShouldReturnSpendJsonWithCorrectFields() {
    SpendJson input = new SpendJson(null, spendEntity.getSpendDate(), categoryJson, CurrencyValues.RUB, 500.0, "Обед", "duck");

    when(categoryService.getOrSave(categoryJson)).thenReturn(categoryEntity);
    when(spendRepository.save(any(SpendEntity.class))).thenReturn(spendEntity);

    SpendJson result = spendService.saveSpendForUser(input);

    assertEquals("duck", result.username());
    assertEquals(500.0, result.amount());
    assertEquals(CurrencyValues.RUB, result.currency());
    assertEquals("Обед", result.description());
    assertEquals("Еда", result.category().name());
  }

  @Test
  void saveSpendForUserShouldPersistCategoryFromCategoryService() {
    SpendJson input = new SpendJson(null, new Date(), categoryJson, CurrencyValues.RUB, 100.0, "Тест", "duck");

    when(categoryService.getOrSave(categoryJson)).thenReturn(categoryEntity);
    when(spendRepository.save(any(SpendEntity.class))).thenReturn(spendEntity);

    spendService.saveSpendForUser(input);

    ArgumentCaptor<SpendEntity> captor = ArgumentCaptor.forClass(SpendEntity.class);
    verify(spendRepository).save(captor.capture());
    assertEquals(categoryEntity, captor.getValue().getCategory());
    assertEquals("duck", captor.getValue().getUsername());
  }

  @Test
  void editSpendForUserShouldThrowSpendNotFoundExceptionWhenNotFound() {
    UUID id = UUID.randomUUID();
    SpendJson input = new SpendJson(id, new Date(), categoryJson, CurrencyValues.RUB, 100.0, "Тест", "duck");

    when(spendRepository.findByIdAndUsername(id, "duck")).thenReturn(Optional.empty());

    SpendNotFoundException ex = assertThrows(
        SpendNotFoundException.class,
        () -> spendService.editSpendForUser(input)
    );
    assertEquals("Can`t find spend by given id: " + id, ex.getMessage());
  }

  @Test
  void editSpendForUserShouldUpdateAllMutableFields() {
    UUID id = spendEntity.getId();
    Date newDate = new Date(0);
    SpendJson input = new SpendJson(id, newDate, categoryJson, CurrencyValues.USD, 999.0, "Ужин", "duck");

    when(spendRepository.findByIdAndUsername(id, "duck")).thenReturn(Optional.of(spendEntity));
    when(categoryService.getOrSave(categoryJson)).thenReturn(categoryEntity);
    when(spendRepository.save(any(SpendEntity.class))).thenReturn(spendEntity);

    spendService.editSpendForUser(input);

    ArgumentCaptor<SpendEntity> captor = ArgumentCaptor.forClass(SpendEntity.class);
    verify(spendRepository).save(captor.capture());
    SpendEntity saved = captor.getValue();
    assertEquals(newDate, saved.getSpendDate());
    assertEquals(CurrencyValues.USD, saved.getCurrency());
    assertEquals(999.0, saved.getAmount());
    assertEquals("Ужин", saved.getDescription());
    assertEquals(categoryEntity, saved.getCategory());
  }

  @Test
  void getSpendForUserShouldThrowSpendNotFoundExceptionForMalformedUuidString() {
    SpendNotFoundException ex = assertThrows(
        SpendNotFoundException.class,
        () -> spendService.getSpendForUser("not-a-valid-uuid", "duck")
    );
    assertEquals("Can`t find spend by given id: not-a-valid-uuid", ex.getMessage());
  }

  @Test
  void getSpendForUserShouldThrowSpendNotFoundExceptionWhenNotFound() {
    UUID id = UUID.randomUUID();
    when(spendRepository.findByIdAndUsername(id, "duck")).thenReturn(Optional.empty());

    SpendNotFoundException ex = assertThrows(
        SpendNotFoundException.class,
        () -> spendService.getSpendForUser(id.toString(), "duck")
    );
    assertEquals("Can`t find spend by given id: " + id, ex.getMessage());
  }

  @Test
  void getSpendForUserShouldReturnSpendJsonWhenFound() {
    UUID id = spendEntity.getId();
    when(spendRepository.findByIdAndUsername(id, "duck")).thenReturn(Optional.of(spendEntity));

    SpendJson result = spendService.getSpendForUser(id.toString(), "duck");

    assertEquals(id, result.id());
    assertEquals("duck", result.username());
    assertEquals(500.0, result.amount());
  }

  @Test
  void deleteSpendsShouldCallRepositoryWithAllParsedUuids() {
    String id1 = UUID.randomUUID().toString();
    String id2 = UUID.randomUUID().toString();

    spendService.deleteSpends("duck", List.of(id1, id2));

    verify(spendRepository).deleteByUsernameAndIdIn(
        eq("duck"),
        argThat(ids -> ids.size() == 2
            && ids.contains(UUID.fromString(id1))
            && ids.contains(UUID.fromString(id2)))
    );
  }

  @Test
  void getSpendsForUserWithCurrencyFilterShouldUseCurrencyFilteredQuery() {
    when(spendRepository.findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
        any(), any(), any(), any()))
        .thenReturn(List.of(spendEntity));

    List<SpendJson> result = spendService.getSpendsForUser("duck", CurrencyValues.RUB, null, null);

    assertEquals(1, result.size());
    verify(spendRepository)
        .findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
            eq("duck"), eq(CurrencyValues.RUB), any(Date.class), any(Date.class));
    verify(spendRepository, never())
        .findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(any(), any(), any());
  }

  @Test
  void getSpendsForUserWithoutCurrencyFilterShouldUseDateOnlyQuery() {
    when(spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
        any(), any(), any()))
        .thenReturn(List.of(spendEntity));

    List<SpendJson> result = spendService.getSpendsForUser("duck", null, null, null);

    assertEquals(1, result.size());
    verify(spendRepository)
        .findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
            eq("duck"), any(Date.class), any(Date.class));
    verify(spendRepository, never())
        .findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(any(), any(), any(), any());
  }

  @Test
  void exportSpendsToCsvShouldProduceCsvWithCorrectHeaderAndDataRow() throws Exception {
    when(spendRepository.findAllByUsernameOrderBySpendDateDesc("duck"))
        .thenReturn(List.of(spendEntity));

    ByteArrayInputStream csvStream = spendService.exportSpendsToCsv("duck");

    List<String[]> rows;
    try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {
      rows = reader.readAll();
    }

    assertEquals(2, rows.size());
    assertArrayEquals(
        new String[]{"Id", "Category", "Description", "Amount", "Currency", "Date"},
        rows.get(0)
    );
    assertEquals(spendEntity.getId().toString(), rows.get(1)[0]);
    assertEquals("Еда", rows.get(1)[1]);
    assertEquals("Обед", rows.get(1)[2]);
    assertEquals("500.0", rows.get(1)[3]);
    assertEquals("RUB", rows.get(1)[4]);
  }

  @Test
  void exportSpendsToCsvShouldReturnEmptyCsvWithHeaderForUserWithNoSpends() throws Exception {
    when(spendRepository.findAllByUsernameOrderBySpendDateDesc("duck"))
        .thenReturn(List.of());

    ByteArrayInputStream csvStream = spendService.exportSpendsToCsv("duck");

    List<String[]> rows;
    try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream, StandardCharsets.UTF_8))) {
      rows = reader.readAll();
    }

    assertEquals(1, rows.size());
    assertArrayEquals(
        new String[]{"Id", "Category", "Description", "Amount", "Currency", "Date"},
        rows.get(0)
    );
  }
}
