package niffler.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public", catalog = "niffler-auth")
public class UserAuthEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private Boolean isEnabled = true;
    @Builder.Default
    @Column(name = "account_non_expired", nullable = false)
    private Boolean isAccountNonExpired = true;
    @Builder.Default
    @Column(name = "account_non_locked", nullable = false)
    private Boolean isAccountNonLocked = true;
    @Builder.Default
    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean isCredentialsNonExpired = true;

    public enum Authority {
        READ, WRITE
    }
}

