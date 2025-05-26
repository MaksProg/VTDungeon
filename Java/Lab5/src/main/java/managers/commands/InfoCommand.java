package managers.commands;

import data.Ticket;
import java.util.Deque;
import managers.CollectionManager;

/**
 * Класс команды которая выводит информацию по коллекции
 *
 * @author Maks
 * @version 1.0
 */
public class InfoCommand implements Command {
  private final CollectionManager collectionManager;

  public InfoCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public void execute(String[] args) {
    Deque<Ticket> tickets = collectionManager.getDequeCollection();
    System.out.println("Информация о коллекции:");
    System.out.println("Тип коллекции: " + tickets.getClass().getSimpleName());
    System.out.println("Дата инициализации: " + collectionManager.getInitDate());
    System.out.println("Количество элементов: " + tickets.size());
  }

  @Override
  public String getDescription() {
    return "выводит информацию по коллекции";
  }
}
