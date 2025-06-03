package common.commands;

import common.managers.CommandManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Map;
import java.util.Scanner;

public class HelpCommand implements Command {
  private CommandManager commandManager;

  public HelpCommand() {}

  public void setCommandManager(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    return new RequestBody(args);
  }

  @Override
  public Response execute(Request request) {
    StringBuilder sb = new StringBuilder("Доступные команды:\n");
    for (Map.Entry<String, Command> entry : commandManager.getCommandList().entrySet()) {
      sb.append(entry.getKey())
          .append(" : ")
          .append(entry.getValue().getDescription())
          .append("\n");
    }
    return new Response(sb.toString().trim());
  }

  @Override
  public String getDescription() {
    return "выводит список доступных команд";
  }
}
