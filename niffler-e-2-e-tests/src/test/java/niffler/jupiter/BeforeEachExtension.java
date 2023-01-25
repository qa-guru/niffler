package niffler.jupiter;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static io.restassured.RestAssured.given;

public class BeforeEachExtension implements AroundEachTest {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    @Override
    public void beforeEachTests() {
        System.out.println("Заносим данные в таблицу spends");
        System.out.println("Вносим данные категории");

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/niffler-spend");
        dataSource.setUsername("postgres");
        dataSource.setPassword("secret");

        new JdbcTemplate(dataSource).execute("INSERT INTO categories (description) VALUES ('Каток');");
    }

    @Override
    public void afterEachTests() {
        System.out.println("Spend cоздан");
    }
}
