package managers.commands;

import data.Ticket;
import managers.CollectionManager;

/**
 * Класс команды которая выводит сумму значений поля price всех билетов
 *
 * @author Maks
 * @version 1.0
 */
public class SumOfPriceCommand implements Command {
  @Override
  public void execute(String[] args) {
    double sum =
        CollectionManager.getDequeCollection().stream().mapToDouble(Ticket::getPrice).sum();

    System.out.println("Сумма всех значений поля price: " + sum);
  }
}
