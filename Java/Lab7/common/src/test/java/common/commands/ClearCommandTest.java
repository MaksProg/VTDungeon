package common.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClearCommandTest {

  private CollectionManager collectionManager;
  private ClearCommand clearCommand;

  @BeforeEach
  void setup() {
    collectionManager = mock(CollectionManager.class);
    clearCommand = new ClearCommand(collectionManager);
  }

  @Test
  void execute_userNotAuthorized_returnsError() {
    Request request = new Request("clear", new RequestBody(new String[] {}), null);

    Response response = clearCommand.execute(request);

    assertTrue(response.getMessage().toLowerCase().contains("ошибка"));
    verifyNoInteractions(collectionManager);
  }

  @Test
  void execute_userAuthorized_clearsCollectionAndReturnsSuccess() {
    AuthCredentials auth = new AuthCredentials("testUser", "password"); // просто создай объект

    Request request = new Request("clear", new RequestBody(new String[] {}), auth);

    when(collectionManager.clearCollection("testUser")).thenReturn(5);

    Response response = clearCommand.execute(request);

    verify(collectionManager, times(1)).clearCollection("testUser");
    assertTrue(response.getMessage().contains("Удалено 5 билетов"));
    assertTrue(response.getMessage().contains("testUser"));
  }

  @Test
  void packageBody_returnsRequestBodyWithArgs() {
    String[] args = {"arg1", "arg2"};
    Scanner scanner = new Scanner(System.in);

    RequestBody body = clearCommand.packageBody(args, scanner);

    assertNotNull(body);
    assertArrayEquals(args, body.getArgs());
  }

  @Test
  void getDescription_returnsNonEmptyString() {
    String description = clearCommand.getDescription();
    assertNotNull(description);
    assertFalse(description.isEmpty());
  }
}
