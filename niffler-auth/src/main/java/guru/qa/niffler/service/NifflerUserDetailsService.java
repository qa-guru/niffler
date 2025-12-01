package guru.qa.niffler.service;

import guru.qa.niffler.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NifflerUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public NifflerUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(ue -> new User(
            ue.getUsername(),
            ue.getPassword(),
            ue.getEnabled(),
            ue.getAccountNonExpired(),
            ue.getCredentialsNonExpired(),
            ue.getAccountNonLocked(),
            ue.getAuthorities().stream().map(
                a -> new SimpleGrantedAuthority(a.getAuthority().name())
            ).toList()
        ))
        .orElseThrow(() -> new UsernameNotFoundException("Username: `" + username + "` not found"));
  }
}
