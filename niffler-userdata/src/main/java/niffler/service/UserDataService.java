package niffler.service;

import niffler.data.CurrencyValues;
import niffler.data.UserEntity;
import niffler.data.repository.UserRepository;
import niffler.model.UserJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDataService {

    private static final CurrencyValues DEFAULT_USER_CURRENCY = CurrencyValues.RUB;
    private final UserRepository userRepository;

    @Autowired
    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserJson update(UserJson user) {
        UserEntity userEntity = userRepository.findByUsername(user.getUserName());
        userEntity.setFirstname(user.getFirstname());
        userEntity.setSurname(user.getSurname());
        userEntity.setCurrency(user.getCurrency());
        userEntity.setPhoto(user.getPhoto());
        UserEntity saved = userRepository.save(userEntity);

        return UserJson.fromEntity(saved);
    }

    public UserJson getCurrentUserOrCreateIfAbsent(String username) {
        UserEntity userDataEntity = userRepository.findByUsername(username);
        if (userDataEntity == null) {
            userDataEntity = new UserEntity();
            userDataEntity.setUsername(username);
            userDataEntity.setCurrency(DEFAULT_USER_CURRENCY);
            return UserJson.fromEntity(userRepository.save(userDataEntity));
        } else {
            return UserJson.fromEntity(userDataEntity);
        }
    }
}
