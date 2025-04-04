package guru.qa.niffler.condition;

import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.condition.spend.Spends;
import guru.qa.niffler.condition.users.Users;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TableConditions {

  @Nonnull
  public static WebElementsCondition spends(SpendJson... expectedSpends) {
    return new Spends(expectedSpends);
  }

  @Nonnull
  public static WebElementsCondition users(UserJson... expectedUsers) {
    return new Users(expectedUsers);
  }
}
