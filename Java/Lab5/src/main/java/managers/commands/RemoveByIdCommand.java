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
  private final CollectionManager collectionManager;

  public RemoveByIdCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public void execute(String[] args) {
    if (args.length < 1) {
      TextColor.errorMessage("Ошибка: укажите id для удаления!");
      return;
    }

    try {
      int id = Integer.parseInt(args[0]);
      boolean removed = collectionManager.removeById(id);

      if (removed) {
        System.out.println(
            TextColor.formatSuccess("Билет с id ") + id + TextColor.formatSuccess(" удалён"));
      } else {
        System.out.println(
            TextColor.formatError("Билет с id") + id + TextColor.formatError(" не найден"));
      }
    } catch (NumberFormatException e) {
      TextColor.errorMessage("Ошибка: id должен быть целым числом!");
    }
  }

  @Override
  public String getDescription() {
    return "удаляет билет по его id";
  }
}
