package guru.qa.niffler.controller;

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

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.model.CurrencyValues.USD;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StatControllerTest {

  private static final String FIXTURE = "/guru/qa/niffler/controller/StatControllerTest.sql";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private GrpcCurrencyClient grpcCurrencyClient;

  @Test
  void getStatisticForUserWithNoSpendsShouldReturnFourZeroEntries() throws Exception {
    mockMvc.perform(get("/internal/stat/total")
            .param("username", "emptyUser")
            .param("userCurrency", RUB.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[0].total").value(0.0))
        .andExpect(jsonPath("$[0].totalInUserDefaultCurrency").value(0.0));
  }

  @Test
  void getStatisticWithCurrencyFilterShouldReturnSingleEntry() throws Exception {
    mockMvc.perform(get("/internal/stat/total")
            .param("username", "emptyUser")
            .param("userCurrency", RUB.name())
            .param("filterCurrency", RUB.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].currency").value(RUB.name()));
  }

  @Test
  @Sql(FIXTURE)
  void getStatisticShouldAccumulateTotalsForUserWithRubSpends() throws Exception {
    when(grpcCurrencyClient.calculate(anyDouble(), eq(RUB), eq(USD)))
        .thenAnswer(inv -> BigDecimal.valueOf((double) inv.getArgument(0) / 75.0));

    mockMvc.perform(get("/internal/stat/total")
            .param("username", "duck")
            .param("userCurrency", USD.name())
            .param("filterCurrency", RUB.name()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].currency").value(RUB.name()))
        .andExpect(jsonPath("$[0].total").value(1500.0))
        .andExpect(jsonPath("$[0].categoryStatistics[0].category").value("Еда"));
  }

  @Test
  @Sql(FIXTURE)
  void getStatisticWithDateRangeFilterShouldReturnOnlyMatchingSpends() throws Exception {
    when(grpcCurrencyClient.calculate(anyDouble(), eq(RUB), eq(RUB)))
        .thenAnswer(inv -> BigDecimal.valueOf((double) inv.getArgument(0)));

    mockMvc.perform(get("/internal/stat/total")
            .param("username", "duck")
            .param("userCurrency", RUB.name())
            .param("filterCurrency", RUB.name())
            .param("from", "2024-10-15")
            .param("to", "2024-10-15"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].total").value(1000.0));
  }
}
