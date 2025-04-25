package managers.commands;

import system.TextColor;

/**
 * Класс команды завершающей работу программы
 *
 * @author Maks
 * @version 1.0
 */
public class ExitCommand implements Command {
  @Override
  public void execute(String[] args) {
    System.out.println(TextColor.ANSI_GREEN + "Завершение программы..." + TextColor.ANSI_RESET);
    System.exit(0);
  }
}
