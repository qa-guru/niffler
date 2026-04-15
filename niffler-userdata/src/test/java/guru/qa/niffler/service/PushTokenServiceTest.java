package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.PushTokenEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.PushTokenRepository;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PushTokenServiceTest {

  private static final String USERNAME = "duck";
  private static final String TOKEN = "fcm-test-token-abc123";
  private static final String USER_AGENT = "TestDevice/1.0";

  private UserEntity buildUser() {
    UserEntity user = new UserEntity();
    user.setId(UUID.randomUUID());
    user.setUsername(USERNAME);
    user.setCurrency(CurrencyValues.RUB);
    return user;
  }

  @Test
  void upsertShouldSaveNewTokenWhenTokenDoesNotExist(@Mock PushTokenRepository tokenRepository,
                                                     @Mock UserRepository userRepository) {
    UserEntity user = buildUser();
    when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.of(user));
    when(tokenRepository.findByToken(eq(TOKEN))).thenReturn(Optional.empty());

    PushTokenService service = new PushTokenService(tokenRepository, userRepository);
    service.upsert(USERNAME, TOKEN, USER_AGENT);

    ArgumentCaptor<PushTokenEntity> captor = ArgumentCaptor.forClass(PushTokenEntity.class);
    verify(tokenRepository).save(captor.capture());
    PushTokenEntity saved = captor.getValue();
    assertEquals(user, saved.getUser());
    assertEquals(TOKEN, saved.getToken());
    assertEquals(USER_AGENT, saved.getUserAgent());
    assertTrue(saved.isActive());
  }

  @Test
  void upsertShouldUpdateExistingTokenWhenTokenAlreadyExists(@Mock PushTokenRepository tokenRepository,
                                                              @Mock UserRepository userRepository) {
    UserEntity user = buildUser();
    PushTokenEntity existingEntity = new PushTokenEntity();
    existingEntity.setToken(TOKEN);
    existingEntity.setActive(false);
    existingEntity.setLastSeenAt(new Date(0));

    when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.of(user));
    when(tokenRepository.findByToken(eq(TOKEN))).thenReturn(Optional.of(existingEntity));

    PushTokenService service = new PushTokenService(tokenRepository, userRepository);
    service.upsert(USERNAME, TOKEN, USER_AGENT);

    verify(tokenRepository, never()).save(any());
    assertEquals(user, existingEntity.getUser());
    assertEquals(USER_AGENT, existingEntity.getUserAgent());
    assertTrue(existingEntity.isActive());
  }

  @Test
  void upsertShouldThrowNotFoundWhenUserDoesNotExist(@Mock PushTokenRepository tokenRepository,
                                                      @Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.empty());
    when(tokenRepository.findByToken(eq(TOKEN))).thenReturn(Optional.empty());

    PushTokenService service = new PushTokenService(tokenRepository, userRepository);
    assertThrows(NotFoundException.class, () -> service.upsert(USERNAME, TOKEN, USER_AGENT));
  }

  @Test
  void tokensForUserShouldReturnActiveTokens(@Mock PushTokenRepository tokenRepository,
                                              @Mock UserRepository userRepository) {
    List<String> tokens = List.of("token-1", "token-2");
    when(tokenRepository.findActiveTokensByUsername(eq(USERNAME))).thenReturn(tokens);

    PushTokenService service = new PushTokenService(tokenRepository, userRepository);
    List<String> result = service.tokensForUser(USERNAME);

    assertEquals(2, result.size());
    assertEquals("token-1", result.get(0));
    assertEquals("token-2", result.get(1));
  }

  @Test
  void tokensForUserShouldReturnEmptyListWhenNoActiveTokens(@Mock PushTokenRepository tokenRepository,
                                                             @Mock UserRepository userRepository) {
    when(tokenRepository.findActiveTokensByUsername(eq(USERNAME))).thenReturn(List.of());

    PushTokenService service = new PushTokenService(tokenRepository, userRepository);
    List<String> result = service.tokensForUser(USERNAME);

    assertTrue(result.isEmpty());
  }

  @Test
  void deactivateShouldSetTokenInactive(@Mock PushTokenRepository tokenRepository,
                                         @Mock UserRepository userRepository) {
    PushTokenEntity entity = new PushTokenEntity();
    entity.setToken(TOKEN);
    entity.setActive(true);

    when(tokenRepository.findByToken(eq(TOKEN))).thenReturn(Optional.of(entity));

    PushTokenService service = new PushTokenService(tokenRepository, userRepository);
    service.deactivate(TOKEN);

    assertFalse(entity.isActive());
  }

  @Test
  void deactivateShouldDoNothingWhenTokenNotFound(@Mock PushTokenRepository tokenRepository,
                                                   @Mock UserRepository userRepository) {
    when(tokenRepository.findByToken(eq(TOKEN))).thenReturn(Optional.empty());

    PushTokenService service = new PushTokenService(tokenRepository, userRepository);
    service.deactivate(TOKEN);

    verify(tokenRepository).findByToken(eq(TOKEN));
  }
}
