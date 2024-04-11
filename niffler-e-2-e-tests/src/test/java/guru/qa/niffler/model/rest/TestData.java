package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record TestData(
        @JsonIgnore String password,
        @JsonIgnore List<CategoryJson> categories,
        @JsonIgnore List<SpendJson> spends,
        @JsonIgnore List<UserJson> friends,
        @JsonIgnore List<UserJson> outcomeInvitations,
        @JsonIgnore List<UserJson> incomeInvitations) {
}
