package niffler.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "friends")
@Table(name = "users", schema = "public", catalog = "niffler-userdata")
public class UsersEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "currency")
    private String currency;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "surname")
    private String surname;

    @Column(name = "photo")
    private byte[] photo;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    @Builder.Default
    private Set<UsersEntity> friends = new HashSet<>();

    public void addFriends(UsersEntity... friends) {
        this.friends.addAll(Arrays.asList(friends));
    }
}
