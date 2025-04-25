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
  @Override
  public void execute(String[] args) {
    System.out.println(TextColor.ANSI_GREEN + "Последние 6 команд:" + TextColor.ANSI_RESET);
    CommandManager.lastSixCommands.forEach(System.out::println);
  }
}
