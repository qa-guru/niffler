package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record TestData(
        @JsonIgnore String password,
        @JsonIgnore List<CategoryJson> categoryJsons,
        @JsonIgnore List<SpendJson> spendJsons,
        @JsonIgnore List<UserJson> friendsJsons,
        @JsonIgnore List<UserJson> invitationsJsons) {
}
