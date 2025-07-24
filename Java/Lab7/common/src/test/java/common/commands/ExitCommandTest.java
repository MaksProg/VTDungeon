package common.commands;

import static org.junit.jupiter.api.Assertions.*;

import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class ExitCommandTest {

  @Test
  void execute_returnsStopResponse() {
    ExitCommand command = new ExitCommand();

    Response response =
        command.execute(new Request("exit", new RequestBody(new String[] {}), null));

    assertEquals("Завершение программы...", response.getMessage());
    assertTrue(response.shouldStopClient());
  }

  @Test
  void packageBody_returnsRequestBodyWithArgs() {
    ExitCommand command = new ExitCommand();

    String[] args = {"arg1", "arg2"};
    Scanner scanner = new Scanner(System.in);

    RequestBody body = command.packageBody(args, scanner);

    assertNotNull(body);
    assertArrayEquals(args, body.getArgs());
  }

  @Test
  void getDescription_returnsExpectedString() {
    ExitCommand command = new ExitCommand();

    String description = command.getDescription();

    assertEquals("выход из приложения", description);
  }
}
