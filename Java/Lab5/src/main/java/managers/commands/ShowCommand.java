package managers.commands;

import managers.CollectionManager;
import system.TextColor;

/**
 * Класс команды которая выводит коллекцию
 *
 * @author Maks
 * @version 1.0
 */
public class ShowCommand implements Command {

  private final CollectionManager collectionManager;

  public ShowCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public void execute(String[] args) {

    if (collectionManager.getDequeCollection().isEmpty()) {
      TextColor.successMessage("Коллекция пуста.");
    } else {
      collectionManager.getDequeCollection().forEach(System.out::println);
    }
  }

  @Override
  public String getDescription() {
    return "выводит коллекцию";
  }
}
