package managers.commands;

import managers.CollectionManager;
import system.TextColor;

/**
 * Класс команды удаляющий билет с определённым ID
 *
 * @author Maks
 * @version 1.0
 */
public class RemoveByIdCommand implements Command {
  @Override
  public void execute(String[] args) {
    if (args.length < 1) {
      System.out.print(
          TextColor.ANSI_RED + "Ошибка: укажите id для удаления!" + TextColor.ANSI_RESET);
      return;
    }

    try {
      int id = Integer.parseInt(args[0]);
      boolean removed = CollectionManager.removeById(id);

      if (removed) {
        System.out.println(
            TextColor.ANSI_GREEN + "Билет с id " + id + " успешно удалён" + TextColor.ANSI_RESET);
      } else {
        System.out.println(
            TextColor.ANSI_RED
                + "Билет с id "
                + id
                + " не найден в коллекции"
                + TextColor.ANSI_RESET);
      }
    } catch (NumberFormatException e) {
      System.out.println(
          TextColor.ANSI_RED + "Ошибка: id должен быть целым числом!" + TextColor.ANSI_RESET);
    }
  }
}
