package niffler.model;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class UserModel {
    private String username;
    private String password;
}
