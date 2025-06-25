package common.managers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.commands.Command;
import common.data.auth.AuthCredentials;
import common.managers.auth.UserManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerCommandManagerTest {

  private ServerCommandManager serverCommandManager;
  private CollectionManager collectionManager;
  private UserManager userManager;

  @BeforeEach
  public void setUp() {
    collectionManager = mock(CollectionManager.class);
    userManager = mock(UserManager.class);
    serverCommandManager = new ServerCommandManager(collectionManager, userManager);
  }

  @Test
  public void handleRequest_commandAddedToHistory() {
    Command realCommand =
        new Command("test_command") {
          @Override
          public Response execute(Request request) {
            return new Response("ok");
          }

          @Override
          public boolean requiresAuth() {
            return true;
          }

          @Override
          public String getDescription() {
            return "Test command description";
          }
        };

    serverCommandManager.getCommandList().put("test_command", realCommand);

    when(userManager.authenticate(any())).thenReturn(1);

    AuthCredentials creds = new AuthCredentials("user", "pass");
    Request request = new Request("test_command", new RequestBody(new String[] {}), creds);

    Response response = serverCommandManager.handleRequest(request);

    assertEquals("ok", response.getMessage());
    assertTrue(serverCommandManager.getHistory().contains("test_command"));
  }

  @Test
  public void handleRequest_commandNotFound() {
    Request request = new Request("nonexistent_command", new RequestBody(new String[] {}), null);
    Response response = serverCommandManager.handleRequest(request);
    assertEquals("Команда не найдена: nonexistent_command", response.getMessage());
  }

  @Test
  public void handleRequest_authRequiredButNotAuthorized() {
    Command realCommand =
        new Command("auth_command") {
          @Override
          public Response execute(Request request) {
            return new Response("should not reach here");
          }

          @Override
          public boolean requiresAuth() {
            return true;
          }

          @Override
          public String getDescription() {
            return "Auth required command";
          }
        };
    serverCommandManager.getCommandList().put("auth_command", realCommand);

    Request request = new Request("auth_command", new RequestBody(new String[] {}), null);

    Response response = serverCommandManager.handleRequest(request);

    assertEquals("Ошибка: требуется авторизация.", response.getMessage());
  }

  @Test
  public void handleRequest_authInvalid() {
    Command realCommand =
        new Command("auth_command") {
          @Override
          public Response execute(Request request) {
            return new Response("should not reach here");
          }

          @Override
          public boolean requiresAuth() {
            return true;
          }

          @Override
          public String getDescription() {
            return "Auth required command";
          }
        };
    serverCommandManager.getCommandList().put("auth_command", realCommand);

    when(userManager.authenticate(any())).thenReturn(null);

    AuthCredentials creds = new AuthCredentials("user", "wrongpass");
    Request request = new Request("auth_command", new RequestBody(new String[] {}), creds);

    Response response = serverCommandManager.handleRequest(request);

    assertEquals("Ошибка: неверный логин или пароль.", response.getMessage());
  }

  @Test
  public void handleRequest_authNotRequired() {
    Command realCommand =
        new Command("no_auth_command") {
          @Override
          public Response execute(Request request) {
            return new Response("no auth needed");
          }

          @Override
          public boolean requiresAuth() {
            return false;
          }

          @Override
          public String getDescription() {
            return "No auth command";
          }
        };

    serverCommandManager.getCommandList().put("no_auth_command", realCommand);

    Request request = new Request("no_auth_command", new RequestBody(new String[] {}), null);

    Response response = serverCommandManager.handleRequest(request);

    assertEquals("no auth needed", response.getMessage());
    assertTrue(serverCommandManager.getHistory().contains("no_auth_command"));
  }
}
