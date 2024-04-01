package guru.qa.niffler.soap;

import guru.qa.niffler.model.UserJson;
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

    protected void enrichUsersResponse(@Nonnull Page<UserJson> users, @Nonnull UsersResponse response) {
        response.getUser().addAll(
                users.getContent().stream()
                        .map(UserJson::toJaxbUser)
                        .toList()
        );
        response.setTotalElements(users.getTotalElements());
        response.setTotalPages(users.getTotalPages());
    }

    protected void enrichUsersResponse(@Nonnull List<UserJson> users, @Nonnull UsersResponse response) {
        response.getUser().addAll(
                users.stream()
                        .map(UserJson::toJaxbUser)
                        .toList()
        );
    }
}
