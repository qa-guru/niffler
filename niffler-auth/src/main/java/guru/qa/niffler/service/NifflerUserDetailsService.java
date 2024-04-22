package guru.qa.niffler.service;

import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.domain.NifflerUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
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
                .map(NifflerUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username: `" + username + "` not found"));
    }
}
