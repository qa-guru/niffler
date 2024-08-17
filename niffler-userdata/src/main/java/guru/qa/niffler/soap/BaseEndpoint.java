package guru.qa.niffler.soap;

import guru.qa.niffler.model.UserJsonBulk;
import jakarta.annotation.Nonnull;
import jaxb.userdata.UsersResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public abstract class BaseEndpoint {

  protected static final String NAMESPACE_URI = "niffler-userdata";

  protected @Nonnull UsersResponse enrichUsersResponse(@Nonnull Page<UserJsonBulk> users,
                                                       @Nonnull UsersResponse response) {
    response.getUser().addAll(
        users.getContent().stream()
            .map(UserJsonBulk::toJaxbUser)
            .toList()
    );
    response.setTotalElements(users.getTotalElements());
    response.setTotalPages(users.getTotalPages());
    return response;
  }

  protected @Nonnull UsersResponse enrichUsersResponse(@Nonnull List<UserJsonBulk> users,
                                                       @Nonnull UsersResponse response) {
    response.getUser().addAll(
        users.stream()
            .map(UserJsonBulk::toJaxbUser)
            .toList()
    );
    return response;
  }
}
