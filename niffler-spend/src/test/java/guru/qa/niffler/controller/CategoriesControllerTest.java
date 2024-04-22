package guru.qa.niffler.controller;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void getCategories() throws Exception {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername("dima");
        categoryEntity.setCategory("Обучение");
        categoryRepository.save(categoryEntity);

        mockMvc.perform(get("/internal/categories/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "dima")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("dima"))
                .andExpect(jsonPath("$[0].category").value("Обучение"));
    }
}