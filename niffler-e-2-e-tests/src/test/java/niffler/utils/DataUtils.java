package niffler.utils;

import com.github.javafaker.Faker;

public class DataUtils {

    private static final Faker faker = new Faker();

    public static String generateRandomUsername() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.bothify("????####");
    }

    public static String generateRandomName() {
        return faker.name().firstName();
    }

    public static String generateRandomSurname() {
        return faker.name().lastName();
    }

    public static String generateNewCategory() {
        return faker.book().title();
    }

    public static String generateRandomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}
