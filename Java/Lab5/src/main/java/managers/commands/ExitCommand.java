package managers.commands;

import system.Console;
import system.TextColor;

/**
 * Класс команды завершающей работу программы
 *
 * @author Maks
 * @version 1.0
 */
public class ExitCommand implements Command {

  private final Console console;

  public ExitCommand(Console console) {
    this.console = console;
  }

  @Override
  public void execute(String[] args) {
    TextColor.successMessage("Завершение программы...");
    console.setRunning(false);
  }

  @Override
  public String getDescription() {
    return "выход из приложения";
  }
}
