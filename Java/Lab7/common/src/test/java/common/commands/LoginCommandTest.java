package common.commands;

import static org.junit.jupiter.api.Assertions.*;

import common.data.auth.AuthCredentials;
import common.exceptions.CommandArgumentException;
import common.managers.auth.UserManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.network.ResponseWithAuthCredentials;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class LoginCommandTest {

  // Простая реализация UserManager для теста
  static class MockUserManager implements UserManager {
    @Override
    public Integer authenticate(AuthCredentials credentials) {
      if ("user".equals(credentials.username()) && "pass".equals(credentials.password())) {
        return 123;
      }
      return null;
    }

    @Override
    public Integer register(AuthCredentials auth) {
      return 0;
    }

    @Override
    public String getUsernameById(int userId) {
      return "";
    }
  }

  @Test
  void testExecuteWithValidCredentials() {
    UserManager userManager = new MockUserManager();
    LoginCommand cmd = new LoginCommand(userManager);

    RequestBody body = new RequestBody(new String[] {"user", "pass"});
    Request req = new Request("login", body, null);

    Response res = cmd.execute(req);

    assertTrue(res instanceof ResponseWithAuthCredentials);
    assertTrue(res.getMessage().contains("успешно"));
  }

  @Test
  void testExecuteWithInvalidCredentials() {
    UserManager userManager = new MockUserManager();
    LoginCommand cmd = new LoginCommand(userManager);

    RequestBody body = new RequestBody(new String[] {"baduser", "badpass"});
    Request req = new Request("login", body, null);

    Response res = cmd.execute(req);

    assertFalse(res instanceof ResponseWithAuthCredentials);
    assertTrue(res.getMessage().contains("неверно"));
  }

  @Test
  void testPackageBodyThrowsOnArgs() {
    LoginCommand cmd = new LoginCommand();

    assertThrows(
        CommandArgumentException.class,
        () -> {
          cmd.packageBody(new String[] {"unexpectedArg"}, new Scanner(System.in));
        });
  }
}
