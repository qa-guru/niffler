package guru.qa.niffler.service;

import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static guru.qa.niffler.service.UserService.DEFAULT_USER_CURRENCY;
import static guru.qa.niffler.service.UserService.isPhotoString;

@Component
public class MigrationService {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationService.class);

  private final UserRepository userRepository;

  @Autowired
  public MigrationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * only for migration V4__small_avatar.sql
   */
  @Transactional
  @PostConstruct
  public void compressAndSaveExistingPhotos() {
    List<UserEntity> users = userRepository.findAll();
    for (UserEntity user : users) {
      if ((user.getPhoto() != null && user.getPhoto().length > 0)
          && (user.getPhotoSmall() == null || user.getPhotoSmall().length == 0)) {
        try {
          String originalPhoto = new String(user.getPhoto(), StandardCharsets.UTF_8);
          if (isPhotoString(originalPhoto)) {
            user.setPhotoSmall(new SmallPhoto(100, 100, originalPhoto).bytes());
            userRepository.save(user);
            LOG.info("### Resizing original user Photo for user done: {}", user.getId());
          }
        } catch (Exception e) {
          LOG.error("### Error while resizing original user Photo for user :{}", user.getId());
        }
      }
    }
  }

  /**
   * only for migration V5__full_name.sql
   */
  @Transactional
  @PostConstruct
  public void setFullName() {
    List<UserEntity> users = userRepository.findAll();
    for (UserEntity user : users) {
      if ((user.getFirstname() != null || user.getSurname() != null) && (user.getFullname() == null)) {
        try {
          final String fullname = user.getFirstname() != null
              ? user.getFirstname().trim() + (" " + user.getSurname()).trim()
              : user.getSurname().trim();
          user.setFullname(fullname);
          userRepository.save(user);
          LOG.info("### Set fullname for user done: {}", user.getId());
        } catch (Exception e) {
          LOG.error("### Error while setting fullname for user :{}", user.getId());
        }
      }
    }
  }

  @Transactional
  @PostConstruct
  public void setUserCurrency() {
    List<UserEntity> users = userRepository.findAll();
    for (UserEntity user : users) {
      if (user.getCurrency() != DEFAULT_USER_CURRENCY) {
        try {
          user.setCurrency(DEFAULT_USER_CURRENCY);
          userRepository.save(user);
          LOG.info("### Set RUB currency for user done: {}", user.getId());
        } catch (Exception e) {
          LOG.error("### Error while setting RUB currency for user :{}", user.getId());
        }
      }
    }
  }
}
