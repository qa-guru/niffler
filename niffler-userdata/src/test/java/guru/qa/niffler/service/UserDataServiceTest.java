package guru.qa.niffler.service;

import static guru.qa.niffler.model.FriendState.FRIEND;
import static guru.qa.niffler.model.FriendState.INVITE_RECEIVED;
import static guru.qa.niffler.model.FriendState.INVITE_SENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendsEntity;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDataServiceTest {

  private UserDataService testDataObject;

  private final UUID mainTestUserUuid = UUID.randomUUID();
  private final String mainTestUserName = "rustam";
  private UserEntity mainTestUser;

  private final UUID secondTestUserUuid = UUID.randomUUID();
  private final String secondTestUserName = "barsik";
  private UserEntity secondTestUser;

  private final UUID thirdTestUserUuid = UUID.randomUUID();
  private final String thirdTestUserName = "emma";
  private UserEntity thirdTestUser;

  private final UUID fourthTestUserUuid = UUID.randomUUID();
  private final String fourthTestUserName = "inna";
  private UserEntity fourthTestUser;

  private final UUID fifthTestUserUuid = UUID.randomUUID();
  private final String fifthTestUserName = "milki";
  private UserEntity fifthTestUser;

  private FriendJson friendJsonUser;


  private final String notExistingUser = "not_existing_user";

  @BeforeEach
  void init() {
    mainTestUser = new UserEntity();
    mainTestUser.setId(mainTestUserUuid);
    mainTestUser.setUsername(mainTestUserName);
    mainTestUser.setCurrency(CurrencyValues.RUB);

    secondTestUser = new UserEntity();
    secondTestUser.setId(secondTestUserUuid);
    secondTestUser.setUsername(secondTestUserName);
    secondTestUser.setCurrency(CurrencyValues.RUB);

    thirdTestUser = new UserEntity();
    thirdTestUser.setId(thirdTestUserUuid);
    thirdTestUser.setUsername(thirdTestUserName);
    thirdTestUser.setCurrency(CurrencyValues.RUB);

    fourthTestUser = new UserEntity();
    fourthTestUser.setId(fourthTestUserUuid);
    fourthTestUser.setUsername(fourthTestUserName);
    fourthTestUser.setCurrency(CurrencyValues.RUB);

    fifthTestUser = new UserEntity();
    fifthTestUser.setId(fifthTestUserUuid);
    fifthTestUser.setUsername(fifthTestUserName);
    fifthTestUser.setCurrency(CurrencyValues.RUB);

    friendJsonUser = new FriendJson();
    friendJsonUser.setUsername(secondTestUserName);
  }

  static Stream<Arguments> userShouldBeUpdated() {
    return Stream.of(
        Arguments.of("rustam")
    );
  }

  @NullSource
  @MethodSource
  @ParameterizedTest
  void userShouldBeUpdated(String photo, @Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(mainTestUserName)))
        .thenReturn(mainTestUser);

    when(userRepository.save(any(UserEntity.class)))
        .thenAnswer(answer -> answer.getArguments()[0]);

    testDataObject = new UserDataService(userRepository);

    final UserJson toBeUpdated = new UserJson();
    toBeUpdated.setUsername(mainTestUserName);
    toBeUpdated.setFirstname("Test");
    toBeUpdated.setSurname("TestSurname");
    toBeUpdated.setCurrency(CurrencyValues.USD);
    toBeUpdated.setPhoto(photo);
    final UserJson result = testDataObject.update(toBeUpdated);
    assertEquals(mainTestUserUuid, result.getId());
    assertEquals("Test", result.getFirstname());
    assertEquals("TestSurname", result.getSurname());
    assertEquals(CurrencyValues.USD, result.getCurrency());
    assertEquals(photo, result.getPhoto());

    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  @Test
  void getRequiredUserShouldThrowNotFoundExceptionIfUserNotFound(@Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(notExistingUser)))
        .thenReturn(null);

    testDataObject = new UserDataService(userRepository);

    final NotFoundException notFoundException = assertThrows(NotFoundException.class,
        () -> testDataObject.getCurrentUser(notExistingUser));
    assertEquals("Can`t find user by username: " + notExistingUser, notFoundException.getMessage());
  }

  @Test
  void allUsersShouldReturnCorrectUsersList(@Mock UserRepository userRepository) {

    testDataObject = new UserDataService(userRepository);

    when(userRepository.findByUsernameNot(eq(mainTestUserName)))
        .thenReturn(getMockUsersMappingFromDb());

    final List<UserJson> users = testDataObject.allUsers(mainTestUserName);
    assertEquals(4, users.size());

    final UserJson invitation = users.stream()
        .filter(u -> u.getFriendState() == INVITE_SENT)
        .findFirst()
        .orElseThrow(() -> new AssertionError("Friend with state INVITE_SENT not found"));

    final UserJson friend = users.stream()
        .filter(u -> u.getFriendState() == FRIEND)
        .findFirst()
        .orElseThrow(() -> new AssertionError("Friend with state FRIEND not found"));

    final UserJson inviteReceived = users.stream()
        .filter(u -> u.getFriendState() == INVITE_RECEIVED)
        .findFirst()
        .orElseThrow(() -> new AssertionError("Friend with state INVITE_RECEIVED not found"));

    assertEquals(thirdTestUserName, friend.getUsername());
//    assertEquals(fifthTestUserName, friend.getUsername());
    assertEquals(secondTestUserName, invitation.getUsername());
    assertEquals(fourthTestUserName, inviteReceived.getUsername());

    List<FriendState> actualStates = new ArrayList<>();
    for (UserJson user: users) {
      actualStates.add(user.getFriendState());
    }
//    List<FriendState> expectedStates = List.of(INVITE_SENT, FRIEND, INVITE_RECEIVED);
    List<FriendState> expectedStates = new ArrayList<>();
    expectedStates.add(INVITE_SENT);
    expectedStates.add(FRIEND);
    expectedStates.add(INVITE_RECEIVED);
    assertThat(actualStates).containsAll(expectedStates);
// todo: написать проверку, что каждый из статусов дружбы есть только один раз
  }

  static Stream<Arguments> friendsShouldBeReturnDifferentListsBasedOnIncludePendingParam() {
    return Stream.of(
        Arguments.of(true, List.of(INVITE_SENT, FRIEND)),
        Arguments.of(false, List.of(FRIEND))
    );
  }

  @MethodSource
  @ParameterizedTest
  void friendsShouldBeReturnDifferentListsBasedOnIncludePendingParam(
      boolean includePending, List<FriendState> expectedStates, @Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(mainTestUserName)))
        .thenReturn(enrichTestUser());

    testDataObject = new UserDataService(userRepository);
    final List<UserJson> result = testDataObject.friends(mainTestUserName, includePending);
    assertEquals(expectedStates.size(), result.size());

    assertTrue(result.stream()
        .map(UserJson::getFriendState)
        .toList()
        .containsAll(expectedStates), "Result should not contains expected states");
  }

  static Stream<Arguments> invitationsShouldBeReturnDifferentListsBasedOnIncludePendingParam() {
    return Stream.of(
        Arguments.of(List.of(INVITE_RECEIVED))
    );
  }

  @MethodSource
  @ParameterizedTest
  void invitationsShouldBeReturnDifferentListsBasedOnIncludePendingParam(
      List<FriendState> expectedStates, @Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(secondTestUserName)))
        .thenReturn(invitesTestUser());

    testDataObject = new UserDataService(userRepository);
    final List<UserJson> result = testDataObject.invitations(secondTestUserName);

    assertTrue(result.stream()
        .map(UserJson::getFriendState)
        .toList()
        .containsAll(expectedStates), "Result should not contains expected states");
  }

  @Test
  void friendShouldBeAdded(@Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(mainTestUserName)))
        .thenReturn(mainTestUser);

    when(userRepository.findByUsername(eq(secondTestUserName)))
        .thenReturn(secondTestUser);

    when(userRepository.save(eq(mainTestUser)))
        .thenAnswer(answer -> answer.getArguments()[0]);

    testDataObject = new UserDataService(userRepository);

    final UserJson result = testDataObject.addFriend(mainTestUserName, friendJsonUser);

    assertEquals(secondTestUser, mainTestUser.getFriends().get(0).getFriend());
    assertTrue(mainTestUser.getFriends().get(0).isPending());
    assertEquals(result.getUsername(), secondTestUserName);
    assertEquals(result.getFriendState(), INVITE_SENT);

    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  static Stream<Arguments> invitationShouldBeAccept() {
    return Stream.of(
        Arguments.of(List.of(FRIEND))
    );
  }

  @MethodSource
  @ParameterizedTest
  void invitationShouldBeAccept(List<FriendState> expectedStates, @Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(mainTestUserName)))
        .thenReturn(mainTestUser);

    mainTestUser.addInvites(secondTestUser);

    when(userRepository.findByUsername(eq(secondTestUserName)))
        .thenReturn(secondTestUser);

    testDataObject = new UserDataService(userRepository);

    final List<UserJson> result = testDataObject.acceptInvitation(mainTestUserName, friendJsonUser);
    assertEquals(expectedStates.size(), result.size());

    assertTrue(result.stream()
        .map(UserJson::getFriendState)
        .toList()
        .containsAll(expectedStates), "Result should not contains expected states");

    verify(userRepository, times(1)).save(any(UserEntity.class));
    assertTrue(mainTestUser.getInvites().get(0).isPending());
  }

  static Stream<Arguments> invitationShouldBeDeclined() {
    return Stream.of(
        Arguments.of(List.of(INVITE_RECEIVED))
    );
  }

  @MethodSource
  @ParameterizedTest
  void invitationShouldBeDeclined(List<FriendState> expectedStates, @Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(mainTestUserName)))
        .thenReturn(mainTestUser);

    mainTestUser.addInvites(secondTestUser);
    secondTestUser.addFriends(true, mainTestUser);

    when(userRepository.findByUsername(eq(secondTestUserName)))
        .thenReturn(secondTestUser);

    testDataObject = new UserDataService(userRepository);

    final List<UserJson> result = testDataObject.declineInvitation(mainTestUserName, friendJsonUser);

    verify(userRepository, times(2)).save(any(UserEntity.class));
    assertTrue(mainTestUser.getInvites().isEmpty());
    assertTrue(secondTestUser.getFriends().isEmpty());
  }

  static Stream<Arguments> friendShouldBeRemoved() {
    return Stream.of(
        Arguments.of(List.of(INVITE_SENT, FRIEND))
    );
  }

  @MethodSource
  @ParameterizedTest
  void friendShouldBeRemoved(List<FriendState> expectedStates, @Mock UserRepository userRepository) {
    when(userRepository.findByUsername(eq(mainTestUserName)))
        .thenReturn(mainTestUser);

    mainTestUser.addInvites(secondTestUser);
    secondTestUser.addFriends(true, mainTestUser);

    secondTestUser.addInvites(mainTestUser);
    mainTestUser.addFriends(true, secondTestUser);

    when(userRepository.findByUsername(eq(secondTestUserName)))
        .thenReturn(secondTestUser);

    testDataObject = new UserDataService(userRepository);

    final List<UserJson> result = testDataObject.removeFriend(mainTestUserName, secondTestUserName);

    verify(userRepository, times(2)).save(any(UserEntity.class));
    verify(userRepository, times(1)).save(eq(mainTestUser));
    verify(userRepository, times(1)).save(eq(secondTestUser));
    assertTrue(mainTestUser.getInvites().isEmpty());
    assertTrue(mainTestUser.getFriends().isEmpty());
    assertTrue(secondTestUser.getInvites().isEmpty());
    assertTrue(secondTestUser.getFriends().isEmpty());
  }

  private UserEntity invitesTestUser() {
    secondTestUser.addInvites(mainTestUser);
    secondTestUser.addInvites(thirdTestUser);

    return secondTestUser;
  }

  private UserEntity enrichTestUser() {
    mainTestUser.addFriends(true, secondTestUser);
    secondTestUser.addInvites(mainTestUser);

    mainTestUser.addFriends(false, thirdTestUser);
    thirdTestUser.addFriends(false, mainTestUser);

    return mainTestUser;
  }

  private List<UserEntity> getMockUsersMappingFromDb() {

    mainTestUser.addFriends(true, secondTestUser);
    secondTestUser.addInvites(mainTestUser);

    mainTestUser.addFriends(false, thirdTestUser);
    thirdTestUser.addFriends(false, mainTestUser);

    fourthTestUser.addFriends(true, mainTestUser);
    mainTestUser.declineInvites(fourthTestUser);

    mainTestUser.addFriends(true, fifthTestUser);
    fifthTestUser.addFriends(true, mainTestUser);

    return List.of(secondTestUser, thirdTestUser, fourthTestUser, fifthTestUser);
  }
}