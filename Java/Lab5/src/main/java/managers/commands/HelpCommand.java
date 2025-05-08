package managers.commands;

import java.util.Map;
import managers.CommandManager;

public class HelpCommand implements Command {
  private CommandManager commandManager;

  public HelpCommand() {}

  public void setCommandManager(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public void execute(String[] args) {
    System.out.println("Доступные команды:");
    for (Map.Entry<String, Command> entry : commandManager.getCommandList().entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue().getDescription());
    }
  }

  @Override
  public String getDescription() {
    return "выводит список доступных команд";
  }
}
