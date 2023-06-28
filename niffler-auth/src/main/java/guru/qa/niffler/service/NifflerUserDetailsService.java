package guru.qa.niffler.service;

import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.domain.NifflerUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class NifflerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public NifflerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new NifflerUserPrincipal(user);
    }
}
