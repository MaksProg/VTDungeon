package managers.commands;

import data.Ticket;
import java.util.ArrayDeque;
import managers.CollectionManager;

/**
 * Класс команды которая выводит информацию по коллекции
 *
 * @author Maks
 * @version 1.0
 */
public class InfoCommand implements Command {
  @Override
  public void execute(String[] args) {
    ArrayDeque<Ticket> tickets = CollectionManager.getDequeCollection();
    System.out.println("Информация о коллекции:");
    System.out.println("Тип коллекции: " + tickets.getClass().getSimpleName());
    System.out.println("Дата инициализации: " + CollectionManager.getInitDate());
    System.out.println("Количество элементов: " + tickets.size());
  }
}
