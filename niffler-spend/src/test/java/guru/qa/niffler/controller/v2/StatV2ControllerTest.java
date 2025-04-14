package guru.qa.niffler.controller.v2;

import guru.qa.niffler.service.GrpcCurrencyClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static guru.qa.niffler.model.CurrencyValues.EUR;
import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.model.CurrencyValues.USD;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StatV2ControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/v2/StatV2ControllerTest.sql";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private GrpcCurrencyClient grpcCurrencyClient;

  @Test
  @Sql(FIXTURE)
  void shouldReturnStatisticForUser() throws Exception {
    when(grpcCurrencyClient.calculate(50.00, USD, RUB))
        .thenReturn(BigDecimal.valueOf(4166.66));

    mockMvc.perform(get("/internal/v2/stat/total")
            .param("username", "duck")
            .param("statCurrency", RUB.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currency").value(RUB.name()))
        .andExpect(jsonPath("$.total").value(5366.66))
        .andExpect(jsonPath("$.statByCategories", hasSize(2)))
        .andExpect(jsonPath("$.statByCategories[0].sum").value(5166.66))
        .andExpect(jsonPath("$.statByCategories[0].currency").value(RUB.name()))
        .andExpect(jsonPath("$.statByCategories[0].categoryName").value("Еда"))
        .andExpect(jsonPath("$.statByCategories[1].sum").value(200.00))
        .andExpect(jsonPath("$.statByCategories[1].currency").value(RUB.name()))
        .andExpect(jsonPath("$.statByCategories[1].categoryName").value("Archived"));
  }

  @Test
  @Sql(FIXTURE)
  void shouldReturnStatisticWithCurrencyFilter() throws Exception {
    when(grpcCurrencyClient.calculate(50.00, USD, RUB))
        .thenReturn(BigDecimal.valueOf(4166.66));

    mockMvc.perform(get("/internal/v2/stat/total")
            .param("username", "duck")
            .param("statCurrency", RUB.name())
            .param("filterCurrency", USD.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currency").value(RUB.name()))
        .andExpect(jsonPath("$.total").value(4166.66))
        .andExpect(jsonPath("$.statByCategories", hasSize(1)))
        .andExpect(jsonPath("$.statByCategories[0].sum").value(4166.66))
        .andExpect(jsonPath("$.statByCategories[0].currency").value(RUB.name()))
        .andExpect(jsonPath("$.statByCategories[0].categoryName").value("Еда"));
  }

  @Test
  @Sql(FIXTURE)
  void shouldConvertCurrencyWhenDifferentFromStatCurrency() throws Exception {
    when(grpcCurrencyClient.calculate(1000.00, RUB, EUR))
        .thenReturn(BigDecimal.valueOf(12.96));
    when(grpcCurrencyClient.calculate(200.00, RUB, EUR))
        .thenReturn(BigDecimal.valueOf(2.59));

    mockMvc.perform(get("/internal/v2/stat/total")
            .param("username", "duck")
            .param("statCurrency", EUR.name())
            .param("filterCurrency", RUB.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currency").value(EUR.name()))
        .andExpect(jsonPath("$.total").value(15.55))
        .andExpect(jsonPath("$.statByCategories", hasSize(2)))
        .andExpect(jsonPath("$.statByCategories[0].sum").value(12.96))
        .andExpect(jsonPath("$.statByCategories[0].currency").value(EUR.name()))
        .andExpect(jsonPath("$.statByCategories[0].categoryName").value("Еда"))
        .andExpect(jsonPath("$.statByCategories[1].sum").value(2.59))
        .andExpect(jsonPath("$.statByCategories[1].currency").value(EUR.name()))
        .andExpect(jsonPath("$.statByCategories[1].categoryName").value("Archived"));
  }

  @Test
  @Sql(FIXTURE)
  void shouldReturnStatisticWithDateFilter() throws Exception {
    when(grpcCurrencyClient.calculate(50.00, USD, RUB))
        .thenReturn(BigDecimal.valueOf(4166.66));

    mockMvc.perform(get("/internal/v2/stat/total")
            .param("username", "duck")
            .param("statCurrency", RUB.name())
            .param("from", "2024-09-05")
            .param("to", "2024-09-06"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.statByCategories", hasSize(1)));
  }

  @Test
  void shouldReturnEmptyStatisticsIfNoSpends() throws Exception {
    mockMvc.perform(get("/internal/v2/stat/total")
            .param("username", "emptyUser")
            .param("statCurrency", "RUB"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.total").value(0.0))
        .andExpect(jsonPath("$.statByCategories", hasSize(0)));
  }
}
