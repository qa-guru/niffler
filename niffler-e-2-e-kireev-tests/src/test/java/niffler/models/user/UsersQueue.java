package niffler.models.user;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.codeborne.selenide.Selenide.sleep;

public final class UsersQueue {

    public static UsersQueue instance(User... users) {
        return instance(List.of(users));
    }

    public static UsersQueue instance(Collection<User> users) {
        UsersQueue usersQueue = new UsersQueue();
        for (User user : users)
            usersQueue.add(user);
        return usersQueue;
    }

    private static final int GET_USER_TIMEOUT_MINUTES = 3;

    // Для хранения множества уникальных пользователей выбран Set, а не Queue
    private final Map<UserRole, Set<User>> userMap = new ConcurrentHashMap<>();

    private UsersQueue() {}

    public User get(UserRole role) {
        if (!userMap.containsKey(role))
            return null;

        for (int i = 1; i <= GET_USER_TIMEOUT_MINUTES * 60; i++) {
            Iterator<User> iterator = userMap.get(role).iterator();
            if (iterator.hasNext()) {
                User user = iterator.next();
                iterator.remove();
                return user;
            } else {
                sleep(1_000);
            }
        }

        return null;
    }

    public void add(User user) {
        if (!userMap.containsKey(user.getRole()))
            userMap.put(user.getRole(), ConcurrentHashMap.newKeySet());
        userMap.get(user.getRole()).add(user);
    }

    public void addAll(UsersQueue usersQueue) {
        usersQueue.userMap.keySet().stream()
                .map(usersQueue.userMap::get)
                .flatMap(Collection::stream)
                .forEach(this::add);
    }

}
