package managers.commands;

import managers.CollectionManager;

public class ShowCommand implements Command {
  /**
   * Класс команды которая выводит коллекцию
   *
   * @author Maks
   * @version 1.0
   */
  @Override
  public void execute(String[] args) {

    if (CollectionManager.getDequeCollection().isEmpty()) {
      System.out.println("Коллекция пуста.");
    } else {
      CollectionManager.getDequeCollection().forEach(ticket -> System.out.println(ticket));
    }
  }
}
