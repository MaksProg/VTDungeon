package managers.commands;

import managers.CollectionManager;
import system.TextColor;

public class ClearCommand implements Command {
  /**
   * Класс команды очищающей коллекцию
   *
   * @author Maks
   * @version 1.0
   */
  @Override
  public void execute(String[] args) {
    CollectionManager.clearCollection();
    System.out.println(TextColor.ANSI_GREEN + "Коллекция успешно очищена." + TextColor.ANSI_RESET);
  }
}
