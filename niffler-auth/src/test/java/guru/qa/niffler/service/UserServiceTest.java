package guru.qa.niffler.service;

import guru.qa.niffler.data.Authority;
import guru.qa.niffler.data.AuthorityEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private KafkaTemplate<String, UserJson> kafkaTemplate;

  @InjectMocks
  private UserService userService;

  @Test
  void registerUserShouldSaveEntityWithEncodedPassword() {
    final String username = "testUser";
    final String rawPassword = "pass123";
    final String encodedPassword = "encoded_pass123";

    UserEntity savedEntity = new UserEntity();
    savedEntity.setUsername(username);

    when(passwordEncoder.encode(eq(rawPassword))).thenReturn(encodedPassword);
    when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

    String result = userService.registerUser(username, rawPassword);

    assertEquals(username, result);

    ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(captor.capture());

    UserEntity captured = captor.getValue();
    assertEquals(username, captured.getUsername());
    assertEquals(encodedPassword, captured.getPassword());
    assertTrue(captured.getEnabled());
    assertTrue(captured.getAccountNonExpired());
    assertTrue(captured.getAccountNonLocked());
    assertTrue(captured.getCredentialsNonExpired());
  }

  @Test
  void registerUserShouldAssignExactlyReadAndWriteAuthorities() {
    final String username = "testUser";

    UserEntity savedEntity = new UserEntity();
    savedEntity.setUsername(username);

    when(passwordEncoder.encode(any())).thenReturn("encoded");
    when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

    userService.registerUser(username, "pass");

    ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(captor.capture());

    List<AuthorityEntity> authorities = captor.getValue().getAuthorities();
    assertEquals(2, authorities.size());
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority() == Authority.read));
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority() == Authority.write));
  }

  @Test
  void registerUserShouldLinkAuthoritiesToUserEntity() {
    final String username = "testUser";

    UserEntity savedEntity = new UserEntity();
    savedEntity.setUsername(username);

    when(passwordEncoder.encode(any())).thenReturn("encoded");
    when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

    userService.registerUser(username, "pass");

    ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(captor.capture());

    UserEntity captured = captor.getValue();
    captured.getAuthorities().forEach(a ->
        assertEquals(captured, a.getUser())
    );
  }

  @Test
  void registerUserShouldSendUsernameToKafkaUsersTopic() {
    final String username = "testUser";

    UserEntity savedEntity = new UserEntity();
    savedEntity.setUsername(username);

    when(passwordEncoder.encode(any())).thenReturn("encoded");
    when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

    userService.registerUser(username, "pass");

    ArgumentCaptor<UserJson> kafkaCaptor = ArgumentCaptor.forClass(UserJson.class);
    verify(kafkaTemplate).send(eq("users"), kafkaCaptor.capture());
    assertEquals(username, kafkaCaptor.getValue().username());
  }

  @Test
  void registerUserShouldPropagateDataIntegrityViolationForDuplicateUsername() {
    when(passwordEncoder.encode(any())).thenReturn("encoded");
    when(userRepository.save(any(UserEntity.class)))
        .thenThrow(new DataIntegrityViolationException("duplicate key"));

    assertThrows(
        DataIntegrityViolationException.class,
        () -> userService.registerUser("existingUser", "pass")
    );
  }

  @Test
  void registerUserShouldNotSendKafkaMessageOnSaveFailure() {
    when(passwordEncoder.encode(any())).thenReturn("encoded");
    when(userRepository.save(any(UserEntity.class)))
        .thenThrow(new DataIntegrityViolationException("duplicate key"));

    assertThrows(
        DataIntegrityViolationException.class,
        () -> userService.registerUser("existingUser", "pass")
    );

    verify(kafkaTemplate, never()).send(any(), any(UserJson.class));
  }
}
