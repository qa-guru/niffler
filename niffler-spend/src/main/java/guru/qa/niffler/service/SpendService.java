package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.projection.SumByCategoryInfo;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.ex.SpendExportException;
import guru.qa.niffler.ex.SpendNotFoundException;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

@Component
@ParametersAreNonnullByDefault
public class SpendService {

  private final SpendRepository spendRepository;
  private final CategoryService categoryService;

  @Autowired
  public SpendService(SpendRepository spendRepository, CategoryService categoryService) {
    this.spendRepository = spendRepository;
    this.categoryService = categoryService;
  }

  @Transactional
  public @Nonnull
  SpendJson saveSpendForUser(SpendJson spend) {
    final String username = spend.username();

    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setUsername(username);
    spendEntity.setSpendDate(spend.spendDate());
    spendEntity.setCurrency(spend.currency());
    spendEntity.setDescription(spend.description());
    spendEntity.setAmount(spend.amount());
    CategoryEntity categoryEntity = categoryService.getOrSave(spend.category());

    spendEntity.setCategory(categoryEntity);
    return SpendJson.fromEntity(spendRepository.save(spendEntity));
  }

  @Transactional
  public @Nonnull
  SpendJson editSpendForUser(SpendJson spend) {
    return spendRepository.findByIdAndUsername(spend.id(), spend.username()).map(
        spendEntity -> {
          CategoryEntity categoryEntity = categoryService.getOrSave(spend.category());
          spendEntity.setSpendDate(spend.spendDate());
          spendEntity.setCategory(categoryEntity);
          spendEntity.setAmount(spend.amount());
          spendEntity.setDescription(spend.description());
          spendEntity.setCurrency(spend.currency());
          return SpendJson.fromEntity(spendRepository.save(spendEntity));
        }
    ).orElseThrow(() -> new SpendNotFoundException(
        "Can`t find spend by given id: " + spend.id()
    ));
  }

  @Transactional(readOnly = true)
  public @Nonnull
  SpendJson getSpendForUser(String id,
                            String username) {
    return spendRepository.findByIdAndUsername(extractUuid(id), username)
        .map(SpendJson::fromEntity)
        .orElseThrow(() -> new SpendNotFoundException(
            "Can`t find spend by given id: " + id
        ));
  }

  @Transactional(readOnly = true)
  public @Nonnull
  List<SpendJson> getSpendsForUser(String username,
                                   @Nullable CurrencyValues filterCurrency,
                                   @Nullable Date dateFrom,
                                   @Nullable Date dateTo) {
    return getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo)
        .stream()
        .map(SpendJson::fromEntity)
        .toList();
  }

  @Transactional(readOnly = true)
  public @Nonnull
  Page<SpendJson> getSpendsForUser(String username,
                                   Pageable pageable,
                                   @Nullable CurrencyValues filterCurrency,
                                   @Nullable Date dateFrom,
                                   @Nullable Date dateTo,
                                   @Nullable String searchQuery) {
    return getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo, searchQuery, pageable)
        .map(SpendJson::fromEntity);
  }

  @Transactional
  public void deleteSpends(String username, List<String> ids) {
    spendRepository.deleteByUsernameAndIdIn(
        username,
        ids.stream().map(UUID::fromString).toList()
    );
  }

  @Transactional(readOnly = true)
  @Nonnull
  List<SpendEntity> getSpendsEntityForUser(String username,
                                           @Nullable CurrencyValues filterCurrency,
                                           @Nullable Date dateFrom,
                                           @Nullable Date dateTo) {
    dateFrom = dateFrom == null ? new Date(0) : dateFrom;
    dateTo = dateTo == null ? new Date() : dateTo;

    if (filterCurrency != null) {
      return spendRepository.findAllByUsernameAndCurrencyAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
          username, filterCurrency, dateFrom, dateTo
      );
    } else {
      return spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqualOrderBySpendDateDesc(
          username, dateFrom, dateTo
      );
    }
  }

  @Transactional(readOnly = true)
  @Nonnull
  List<SumByCategoryInfo> getSumByCategories(String username,
                                             @Nullable Date dateFrom,
                                             @Nullable Date dateTo) {
    dateFrom = dateFrom == null ? new Date(0) : dateFrom;
    dateTo = dateTo == null ? new Date() : dateTo;

    List<SumByCategoryInfo> result = new ArrayList<>();
    result.addAll(spendRepository.statisticByArchivedCategory(username, dateFrom, dateTo));
    result.addAll(spendRepository.statisticByCategory(username, dateFrom, dateTo));
    return result;
  }

  @Transactional(readOnly = true)
  @Nonnull
  Page<SpendEntity> getSpendsEntityForUser(String username,
                                           @Nullable CurrencyValues filterCurrency,
                                           @Nullable Date dateFrom,
                                           @Nullable Date dateTo,
                                           @Nullable String searchQuery,
                                           Pageable pageable) {
    dateTo = dateTo == null
        ? new Date()
        : dateTo;

    dateFrom = dateFrom == null
        ? new Date(0)
        : dateFrom;

    Page<SpendEntity> spends;
    if (filterCurrency != null) {
      if (searchQuery != null) {
        spends = spendRepository.findAll(username, filterCurrency, dateFrom, dateTo, searchQuery, pageable);
      } else {
        spends = spendRepository.findAll(username, filterCurrency, dateFrom, dateTo, pageable);
      }
    } else {
      if (searchQuery != null) {
        spends = spendRepository.findAll(username, dateFrom, dateTo, searchQuery, pageable);
      } else {
        spends = spendRepository.findAll(username, dateFrom, dateTo, pageable);
      }
    }
    return spends;
  }

  @Transactional(readOnly = true)
  public @Nonnull ByteArrayInputStream exportSpendsToCsv(String username) {
    final List<SpendEntity> spends = spendRepository.findAllByUsernameOrderBySpendDateDesc(username);
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
         CSVPrinter csvPrinter = new CSVPrinter(
             new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)),
             DEFAULT.builder().setHeader(
                 "Id", "Category", "Description", "Amount", "Currency", "Date"
             ).get()
         )) {
      for (SpendEntity spend : spends) {
        csvPrinter.printRecord(
            spend.getId(),
            spend.getCategory().getName(),
            spend.getDescription(),
            spend.getAmount(),
            spend.getCurrency(),
            spend.getSpendDate()
        );
      }
      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new SpendExportException("Error generating CSV for user: " + username);
    }
  }

  private @Nonnull UUID extractUuid(String id) {
    final UUID spendId;
    try {
      spendId = UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new SpendNotFoundException(
          "Can`t find spend by given id: " + id
      );
    }
    return spendId;
  }
}
