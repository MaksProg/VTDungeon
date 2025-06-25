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

class RegisterCommandTest {

  static class MockUserManager implements UserManager {
    @Override
    public Integer authenticate(AuthCredentials auth) {
      return 0;
    }

    @Override
    public Integer register(AuthCredentials credentials) {
      if ("takenUser".equals(credentials.username())) {
        return null;
      }
      return 42;
    }

    @Override
    public String getUsernameById(int userId) {
      return "";
    }
  }

  @Test
  void testExecuteRegisterSuccess() {
    UserManager userManager = new MockUserManager();
    RegisterCommand cmd = new RegisterCommand(userManager);

    RequestBody body = new RequestBody(new String[] {"newUser", "newPass"});
    Request req = new Request("register", body, null);

    Response res = cmd.execute(req);

    assertTrue(res instanceof ResponseWithAuthCredentials);
    assertTrue(res.getMessage().contains("успешно"));
    assertTrue(
        ((ResponseWithAuthCredentials) res).getAuthCredentials().username().equals("newUser"));
  }

  @Test
  void testExecuteRegisterNameTaken() {
    UserManager userManager = new MockUserManager();
    RegisterCommand cmd = new RegisterCommand(userManager);

    RequestBody body = new RequestBody(new String[] {"takenUser", "pass"});
    Request req = new Request("register", body, null);

    Response res = cmd.execute(req);

    assertFalse(res instanceof ResponseWithAuthCredentials);
    assertTrue(res.getMessage().contains("уже занято"));
  }

  @Test
  void testPackageBodyThrowsOnArgs() {
    RegisterCommand cmd = new RegisterCommand();

    assertThrows(
        CommandArgumentException.class,
        () -> {
          cmd.packageBody(new String[] {"arg"}, new Scanner(System.in));
        });
  }
}
