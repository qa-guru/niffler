package guru.qa.niffler.service;

import guru.qa.niffler.data.PushTokenEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.PushTokenRepository;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;

@ParametersAreNonnullByDefault
@Service
public class PushTokenService {

  private final PushTokenRepository tokenRepository;
  private final UserRepository userRepository;

  @Autowired
  public PushTokenService(PushTokenRepository tokenRepository, UserRepository userRepository) {
    this.tokenRepository = tokenRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public void upsert(String username, String token, String userAgent) {
    tokenRepository.findByToken(token).ifPresentOrElse(
        e -> {
          e.setUser(getRequiredUser(username));
          e.setUserAgent(userAgent);
          e.setActive(true);
          e.setLastSeenAt(new Date());
        },
        () -> {
          PushTokenEntity e = new PushTokenEntity();
          e.setUser(getRequiredUser(username));
          e.setToken(token);
          e.setUserAgent(userAgent);
          e.setActive(true);
          e.setCreatedAt(new Date());
          e.setLastSeenAt(new Date());
          tokenRepository.save(e);
        }
    );
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<String> tokensForUser(String username) {
    return tokenRepository.findActiveTokensByUsername(username);
  }

  @Transactional
  public void deactivate(String token) {
    tokenRepository.findByToken(token).ifPresent(e -> e.setActive(false));
  }

  @Nonnull
  UserEntity getRequiredUser(String username) {
    return userRepository.findByUsername(username).orElseThrow(
        () -> new NotFoundException("Can`t find user by username: '" + username + "'")
    );
  }
}
