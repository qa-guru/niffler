package guru.qa.niffler.data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UserEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Boolean enabled;

  @Column(name = "account_non_expired", nullable = false)
  private Boolean accountNonExpired;

  @Column(name = "account_non_locked", nullable = false)
  private Boolean accountNonLocked;

  @Column(name = "credentials_non_expired", nullable = false)
  private Boolean credentialsNonExpired;

  @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
  private List<AuthorityEntity> authorities = new ArrayList<>();

  public void addAuthorities(AuthorityEntity... authorities) {
    for (AuthorityEntity authority : authorities) {
      this.authorities.add(authority);
      authority.setUser(this);
    }
  }

  public void removeAuthority(AuthorityEntity authority) {
    this.authorities.remove(authority);
    authority.setUser(null);
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    UserEntity that = (UserEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
