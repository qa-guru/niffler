package guru.qa.niffler.soap;

import guru.qa.niffler.model.UserJsonBulk;
import jakarta.annotation.Nonnull;
import niffler_userdata.UsersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class BaseEndpoint {

    protected static final String NAMESPACE_URI = "niffler-userdata";

    protected @Nonnull Sort sortFromRequest(@Nonnull List<niffler_userdata.Sort> sort) {
        return Sort.by(sort.stream()
                .map(st -> new Sort.Order(
                                Sort.Direction.fromString(
                                        st.getDirection().name()
                                ),
                                st.getProperty()
                        )
                ).toList());
    }

    protected void enrichUsersResponse(@Nonnull Page<UserJsonBulk> users, @Nonnull UsersResponse response) {
        response.getUser().addAll(
                users.getContent().stream()
                        .map(UserJsonBulk::toJaxbUser)
                        .toList()
        );
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
    }

    protected void enrichUsersResponse(@Nonnull List<UserJsonBulk> users, @Nonnull UsersResponse response) {
        response.getUser().addAll(
                users.stream()
                        .map(UserJsonBulk::toJaxbUser)
                        .toList()
        );
    }
}
