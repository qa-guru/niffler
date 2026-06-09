package guru.qa.niffler.page.component;

import com.codeborne.selenide.DownloadOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.SelenideElement;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import guru.qa.niffler.model.rest.DataFilterValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.TableConditions.spends;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable> {

  private final SearchField searchField = new SearchField();
  private final SelenideElement periodMenu = self.$("#period");
  private final SelenideElement currencyMenu = self.$("#currency");
  private final ElementsCollection menuItems = $$(".MuiList-padding li");
  private final SelenideElement deleteBtn = self.$("#delete");
  private final SelenideElement contextMenuBtn = self.$("#spending-menu");
  private final SelenideElement csvMenu = $("ul[role='menu']");
  private final ElementsCollection csvMenuItems = csvMenu.$$("li");
  private final SelenideElement downloadComponent = csvMenuItems.find(exactText("Export to CSV"));
  private final SelenideElement popup = $("div[role='dialog']");

  private final SelenideElement tableHeader = self.$(".MuiTableHead-root");
  private final ElementsCollection headerCells = tableHeader.$$(".MuiTableCell-root");

  private final ElementsCollection tableRows = self.$("tbody").$$("tr");


  public SpendingTable() {
    super($("#spendings"));
  }

  @Step("Select table period {0}")
  @Nonnull
  public SpendingTable selectPeriod(DataFilterValues period) {
    periodMenu.click();
    menuItems.find(text(period.text)).click();
    return this;
  }

  @Step("Edit spending with description {0}")
  @Nonnull
  public EditSpendingPage editSpending(String description) {
    searchSpendingByDescription(description);
    SelenideElement row = tableRows.find(text(description));
    row.$$("td").get(5).click();
    return new EditSpendingPage();
  }

  @Step("Delete spending with description {0}")
  @Nonnull
  public SpendingTable deleteSpending(String description) {
    searchSpendingByDescription(description);
    SelenideElement row = tableRows.find(text(description));
    row.$$("td").get(0).click();
    deleteBtn.click();
    popup.$(byText("Delete")).click(usingJavaScript());
    return this;
  }

  @Step("Open spending context menu")
  @Nonnull
  public SpendingTable openContextMenu() {
    contextMenuBtn.click();
    csvMenu.shouldBe(visible);
    return this;
  }

  @Step("Check spending context menu CSV actions")
  @Nonnull
  public SpendingTable checkCsvActionsVisible() {
    csvMenuItems.find(exactText("Export to CSV")).shouldBe(visible);
    csvMenuItems.find(exactText("Import CSV")).shouldBe(visible)
        .shouldHave(attribute("aria-disabled", "true"));
    return this;
  }

  @Step("Export spendings to CSV")
  @Nonnull
  public File exportCsv() throws IOException, InterruptedException {
    return downloadComponent.download(
        DownloadOptions.using(FileDownloadMode.FOLDER).withExtension("csv")
    );
  }

  @Step("Check CSV file contains exported spendings")
  @Nonnull
  public SpendingTable checkCsvContains(File csvFile, SpendJson... expectedSpends) throws IOException, CsvException {
    final List<String[]> rows;
    try (CSVReader reader = new CSVReader(Files.newBufferedReader(csvFile.toPath(), StandardCharsets.UTF_8))) {
      rows = reader.readAll();
    }
    assertArrayEquals(
        new String[]{"Id", "Category", "Description", "Amount", "Currency", "Date"},
        rows.getFirst()
    );
    for (SpendJson expectedSpend : expectedSpends) {
      final String[] csvRow = rowByDescription(rows, expectedSpend.description());
      assertAll(() -> {
        assertTrue(csvRow[0].matches("[0-9a-fA-F-]{36}"), "CSV should contain spend id");
        assertEquals(expectedSpend.category().name(), csvRow[1]);
        assertEquals(expectedSpend.description(), csvRow[2]);
        assertEquals(String.valueOf(expectedSpend.amount()), csvRow[3]);
        assertEquals(expectedSpend.currency().name(), csvRow[4]);
      });
    }
    return this;
  }

  @Step("Search spending with description {0}")
  @Nonnull
  public SpendingTable searchSpendingByDescription(String description) {
    searchField.search(description);
    return this;
  }

  @Step("Clear spending search")
  @Nonnull
  public SpendingTable clearSearch() {
    searchField.clear();
    return this;
  }

  @Step("Check that table contains spending description {0}")
  @Nonnull
  public SpendingTable checkTableContainsDescription(String description) {
    tableRows.find(text(description)).shouldHave(text(description));
    return this;
  }

  @Step("Check that table does not contain spending description {0}")
  @Nonnull
  public SpendingTable checkTableDoesNotContainDescription(String description) {
    tableRows.find(text(description)).shouldNot(exist);
    return this;
  }

  @Step("Check that table contains data {0}")
  @Nonnull
  public SpendingTable checkTableContains(SpendJson... expectedSpends) {
    tableRows.should(spends(expectedSpends));
    return this;
  }

  @Step("Check that table have size {0}")
  @Nonnull
  public SpendingTable checkTableSize(int expectedSize) {
    tableRows.should(size(expectedSize));
    return this;
  }

  @Step("Check that spending with description {0} has date text {1}")
  @Nonnull
  public SpendingTable checkSpendingDateText(String description, String expectedDateText) {
    searchSpendingByDescription(description);
    SelenideElement row = tableRows.find(text(description));
    row.$$("td").get(4).should(exactText(expectedDateText));
    return this;
  }

  private String[] rowByDescription(List<String[]> csvRows, String description) {
    return csvRows.stream()
        .skip(1)
        .filter(row -> description.equals(row[2]))
        .findFirst()
        .orElse(null);
  }

}
