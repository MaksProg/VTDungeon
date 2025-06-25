package common.managers;

import common.commands.Command;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class FakeCommandManager extends CommandManager {

  public FakeCommandManager() {
    super();
  }

  @Override
  protected void initCommands() {
    commandList = new LinkedHashMap<>();
    commandList.put("mock", new MockCommand());
  }

  static class MockCommand extends Command {
    public MockCommand() {
      super("mock");
    }

    @Override
    public RequestBody packageBody(String[] args, Scanner in) {
      return new RequestBody(args);
    }

    @Override
    public Response execute(Request request) {
      return new Response("Mock response");
    }

    @Override
    public String getDescription() {
      return "Mock command";
    }
  }
}
