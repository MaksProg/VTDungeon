package server.userManagers;

import static org.junit.jupiter.api.Assertions.*;

import common.data.auth.AuthCredentials;
import java.sql.*;
import org.junit.jupiter.api.*;

class SqlUserManagerTest {

  private Connection connection;
  private SqlUserManager userManager;

  @BeforeEach
  void setupDatabase() throws Exception {
    connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
    userManager = new SqlUserManager(connection);
  }

  @AfterEach
  void teardown() throws Exception {
    connection.close();
  }

  @Test
  void testRegisterAndAuthenticate() {
    AuthCredentials auth = new AuthCredentials("testUser", "securePassword");

    Integer registeredId = userManager.register(auth);
    assertNotNull(registeredId, "Регистрация должна вернуть ID");

    Integer authenticatedId = userManager.authenticate(auth);
    assertNotNull(authenticatedId, "Аутентификация должна пройти успешно");
    assertEquals(registeredId, authenticatedId, "ID должен совпадать после аутентификации");
  }

  @Test
  void testRegisterWithSameUsernameFails() {
    AuthCredentials auth = new AuthCredentials("duplicateUser", "pass123");

    Integer id1 = userManager.register(auth);
    assertNotNull(id1, "Первая регистрация должна быть успешной");

    Integer id2 = userManager.register(auth);
    assertNull(id2, "Повторная регистрация должна вернуть null");
  }

  @Test
  void testAuthenticateFailsWithWrongPassword() {
    AuthCredentials correct = new AuthCredentials("someUser", "correctPass");
    userManager.register(correct);

    AuthCredentials wrong = new AuthCredentials("someUser", "wrongPass");
    Integer result = userManager.authenticate(wrong);
    assertNull(result, "Аутентификация с неверным паролем должна вернуть null");
  }

  @Test
  void testGetUsernameById() {
    AuthCredentials auth = new AuthCredentials("lookupUser", "1234");
    Integer id = userManager.register(auth);
    assertNotNull(id, "Регистрация должна пройти успешно");

    String username = userManager.getUsernameById(id);
    assertEquals("lookupUser", username, "Имя пользователя должно совпадать");
  }

  @Test
  void testGetUsernameByInvalidIdReturnsNull() {
    String result = userManager.getUsernameById(-123);
    assertNull(result, "При неверном ID должен быть возвращён null");
  }
}
