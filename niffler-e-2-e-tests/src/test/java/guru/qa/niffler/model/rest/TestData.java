package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record TestData(
    @JsonIgnore @Nonnull String password,
    @JsonIgnore @Nonnull List<CategoryJson> categories,
    @JsonIgnore @Nonnull List<SpendJson> spends,
    @JsonIgnore @Nonnull List<UserJson> friends,
    @JsonIgnore @Nonnull List<UserJson> outcomeInvitations,
    @JsonIgnore @Nonnull List<UserJson> incomeInvitations) {

  public TestData(@Nonnull String password) {
    this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public @Nonnull String[] friendsUsernames() {
    return extractUsernames(friends);
  }

  public @Nonnull String[] incomeInvitationsUsernames() {
    return extractUsernames(incomeInvitations);
  }

  public @Nonnull String[] outcomeInvitationsUsernames() {
    return extractUsernames(outcomeInvitations);
  }

  private @Nonnull String[] extractUsernames(List<UserJson> users) {
    return users.stream().map(UserJson::username).toArray(String[]::new);
  }
}
