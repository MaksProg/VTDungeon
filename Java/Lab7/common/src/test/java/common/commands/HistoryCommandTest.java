package common.commands;

import static org.junit.jupiter.api.Assertions.*;

import common.data.auth.AuthCredentials;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithHistory;
import common.network.Response;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryCommandTest {

  private HistoryCommand command;

  @BeforeEach
  void setUp() {
    command = new HistoryCommand();
  }

  @Test
  void execute_notAuthorized_returnsError() {
    Request request = new Request("history", new RequestBody(new String[] {}), null);

    Response response = command.execute(request);

    assertEquals(
        "Ошибка: необходимо авторизоваться для просмотра истории команд.", response.getMessage());
  }

  @Test
  void execute_noHistory_returnsEmptyMessage() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    ConcurrentLinkedDeque<String> emptyHistory = new ConcurrentLinkedDeque<>();
    RequestBodyWithHistory body = new RequestBodyWithHistory(new String[] {}, emptyHistory);

    Request request = new Request("history", body, auth);

    Response response = command.execute(request);

    assertEquals("История команд пуста.", response.getMessage());
  }

  @Test
  void execute_withHistory_returnsLastCommands() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    ConcurrentLinkedDeque<String> history = new ConcurrentLinkedDeque<>();
    history.add("help");
    history.add("info");
    history.add("add");
    history.add("remove");
    history.add("clear");
    history.add("exit");

    RequestBodyWithHistory body = new RequestBodyWithHistory(new String[] {}, history);
    Request request = new Request("history", body, auth);

    Response response = command.execute(request);

    String expected = "Последние 6 команд:\nhelp\ninfo\nadd\nremove\nclear\nexit";

    assertEquals(expected, response.getMessage());
  }

  @Test
  void execute_bodyNotWithHistory_returnsError() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    RequestBody plainBody = new RequestBody(new String[] {});
    Request request = new Request("history", plainBody, auth);

    Response response = command.execute(request);

    assertEquals("Ошибка: не передана история команд.", response.getMessage());
  }

  @Test
  void packageBody_returnsRequestBodyWithArgs() {
    String[] args = {"arg1", "arg2"};
    RequestBody body = command.packageBody(args, null);
    assertNotNull(body);
    assertArrayEquals(args, body.getArgs());
  }

  @Test
  void getDescription_returnsCorrectString() {
    assertEquals("выводит последние 6 команд без аргументов", command.getDescription());
  }
}
