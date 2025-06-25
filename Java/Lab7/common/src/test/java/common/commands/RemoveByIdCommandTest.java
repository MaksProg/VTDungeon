package common.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.data.auth.AuthCredentials;
import common.exceptions.CommandArgumentException;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RemoveByIdCommandTest {

  private CollectionManager collectionManager;
  private RemoveByIdCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);
    command = new RemoveByIdCommand(collectionManager);
  }

  @Test
  void execute_userNotAuthorized_returnsError() {
    Request request = new Request("remove_by_id", new RequestBody(new String[] {"1"}), null);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("пользователь не авторизован"));
  }

  @Test
  void execute_invalidId_returnsError() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Request request =
        new Request("remove_by_id", new RequestBody(new String[] {"not_a_number"}), auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("id должен быть целым числом"));
  }

  @Test
  void execute_removeSuccess_returnsSuccessMessage() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    when(collectionManager.removeById(5, "user")).thenReturn(true);

    Request request = new Request("remove_by_id", new RequestBody(new String[] {"5"}), auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("удалён"));
    verify(collectionManager).removeById(5, "user");
  }

  @Test
  void execute_removeFail_returnsErrorMessage() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    when(collectionManager.removeById(7, "user")).thenReturn(false);

    Request request = new Request("remove_by_id", new RequestBody(new String[] {"7"}), auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("Не удалось удалить билет"));
    verify(collectionManager).removeById(7, "user");
  }

  @Test
  void packageBody_invalidArgs_throwsException() {
    String[] args = new String[] {};
    String[] finalArgs = args;
    assertThrows(
        CommandArgumentException.class,
        () -> command.packageBody(finalArgs, new Scanner(System.in)));

    args = new String[] {"notANumber"};
    String[] finalArgs1 = args;
    assertThrows(
        CommandArgumentException.class,
        () -> command.packageBody(finalArgs1, new Scanner(System.in)));
  }

  @Test
  void packageBody_validArgs_returnsRequestBody() throws CommandArgumentException {
    String[] args = new String[] {"123"};
    RequestBody body = command.packageBody(args, new Scanner(System.in));
    assertArrayEquals(args, body.getArgs());
  }

  @Test
  void getDescription_returnsExpectedString() {
    assertEquals(
        "удаляет билет по его id, если он принадлежит текущему пользователю",
        command.getDescription());
  }
}
