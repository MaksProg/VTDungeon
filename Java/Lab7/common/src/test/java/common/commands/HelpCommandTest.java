package common.commands;

import static org.junit.jupiter.api.Assertions.*;

import common.managers.FakeCommandManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpCommandTest {

  private HelpCommand helpCommand;
  private FakeCommandManager fakeCommandManager;

  @BeforeEach
  void setUp() {
    helpCommand = new HelpCommand();
    fakeCommandManager = new FakeCommandManager();
    helpCommand.setCommandManager(fakeCommandManager);
  }

  @Test
  void execute_returnsListOfCommands() {
    Request request = new Request("help", new RequestBody(new String[] {}), null);

    Response response = helpCommand.execute(request);

    String expected = "Доступные команды:\n" + "mock : Mock command";

    assertEquals(expected, response.getMessage());
  }

  @Test
  void packageBody_returnsRequestBodyWithArgs() {
    String[] args = {"arg1", "arg2"};
    Scanner scanner = new Scanner(System.in);

    RequestBody body = helpCommand.packageBody(args, scanner);

    assertNotNull(body);
    assertArrayEquals(args, body.getArgs());
  }

  @Test
  void getDescription_returnsCorrectDescription() {
    assertEquals("выводит список доступных команд", helpCommand.getDescription());
  }
}
