package managers.commands;

import managers.CommandManager;
import system.TextColor;

/**
 * Класс команды которая выводит последние 6 команд без аргументов
 *
 * @author Maks
 * @version 1.0
 */
public class HistoryCommand implements Command {
  private CommandManager commandManager;

  public HistoryCommand() {}

  public void setCommandManager(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  @Override
  public void execute(String[] args) {
    TextColor.successMessage("Последние 6 команд:");
    commandManager.lastSixCommands.forEach(System.out::println);
  }

  @Override
  public String getDescription() {
    return "выводит последние 6 команд без аргументов";
  }
}
