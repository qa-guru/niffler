package guru.qa.niffler.service;

import guru.qa.niffler.data.Authority;
import guru.qa.niffler.data.AuthorityEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class NifflerUserDetailsServiceTest {

    private NifflerUserDetailsService nifflerUserDetailsService;
    private UserEntity testUserEntity;
    private List<AuthorityEntity> authorityEntities;

    @BeforeEach
    void initMockRepository(@Mock UserRepository userRepository) {
        AuthorityEntity read = new AuthorityEntity();
        read.setUser(testUserEntity);
        read.setAuthority(Authority.read);
        AuthorityEntity write = new AuthorityEntity();
        write.setUser(testUserEntity);
        write.setAuthority(Authority.write);
        authorityEntities = List.of(read, write);

        testUserEntity = new UserEntity();
        testUserEntity.setUsername("correct");
        testUserEntity.setAuthorities(authorityEntities);
        testUserEntity.setEnabled(true);
        testUserEntity.setPassword("test-pass");
        testUserEntity.setAccountNonExpired(true);
        testUserEntity.setAccountNonLocked(true);
        testUserEntity.setCredentialsNonExpired(true);
        testUserEntity.setId(UUID.randomUUID());

        lenient().when(userRepository.findByUsername("correct"))
                .thenReturn(Optional.of(testUserEntity));

        lenient().when(userRepository.findByUsername(not(eq("correct"))))
                .thenReturn(Optional.empty());

        nifflerUserDetailsService = new NifflerUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername() {
        final UserDetails correct = nifflerUserDetailsService.loadUserByUsername("correct");

        final List<SimpleGrantedAuthority> expectedAuthorities = authorityEntities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
                .toList();

        assertEquals(
                "correct",
                correct.getUsername()
        );
        assertEquals(
                "test-pass",
                correct.getPassword()
        );
        assertEquals(
                expectedAuthorities,
                correct.getAuthorities()
        );

        assertTrue(correct.isAccountNonExpired());
        assertTrue(correct.isAccountNonLocked());
        assertTrue(correct.isCredentialsNonExpired());
        assertTrue(correct.isEnabled());
    }

    @Test
    void loadUserByUsernameNegative() {
        final UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> nifflerUserDetailsService.loadUserByUsername("incorrect")
        );

        assertEquals(
                "Username: `incorrect` not found",
                exception.getMessage()
        );
    }
}