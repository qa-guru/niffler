package guru.qa.niffler.model.gql;

import java.util.UUID;

public record CategoryGqlInput(
    UUID id,
    String name,
    boolean archived
) {
}
