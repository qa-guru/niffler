package niffler.jupiter.lesson1;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class BeforeSuiteExtension implements AroundAllTestsExtension {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    @Override
    public void beforeAllTests() {
        System.out.println("Вносим данные категории");

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/niffler-spend");
        dataSource.setUsername("postgres");
        dataSource.setPassword("secret");

        new JdbcTemplate(dataSource).execute("INSERT INTO categories (description) VALUES ('Каток');");


    }

    @Override
    public void afterAllTests() {
        System.out.println("Удаляем данные категории");

        new JdbcTemplate(dataSource).execute("DELETE FROM spends WHERE description = 'Покатушки' ;");
        new JdbcTemplate(dataSource).execute("DELETE FROM categories WHERE description = 'Каток' ;");
    }
}
