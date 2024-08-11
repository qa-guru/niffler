package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;

public class DataUtils {

  private static final Faker faker = new Faker();

  @Nonnull
  public static String generateRandomUsername() {
    return faker.name().username();
  }

  @Nonnull
  public static String generateRandomPassword() {
    return faker.bothify("????####");
  }

  @Nonnull
  public static String generateRandomName() {
    return faker.name().firstName();
  }

  @Nonnull
  public static String generateRandomSurname() {
    return faker.name().lastName();
  }

  @Nonnull
  public static String generateNewCategory() {
    return faker.food().fruit();
  }

  @Nonnull
  public static String generateRandomSentence(int wordsCount) {
    return faker.lorem().sentence(wordsCount);
  }
}
