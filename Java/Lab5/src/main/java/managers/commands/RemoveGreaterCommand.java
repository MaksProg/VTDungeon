package managers.commands;

import data.Ticket;
import java.util.Iterator;
import managers.CollectionManager;
import system.TextColor;

/**
 * Класс реализующий команду которая удаляет все билеты цена которых больше той, которую ввёл пользователь
 * @author Maks
 * @version 1.0
 */
public class RemoveGreaterCommand implements Command {
  @Override
  public void execute(String[] args) {
    if (args.length == 0) {
      System.out.println("Укажите цену для сравнения");
      return;
    }

    try {
      double priceThreshold = Double.parseDouble(args[0]);
      Iterator<Ticket> iterator = CollectionManager.getDequeCollection().iterator();

      int removedCount = 0;
      while (iterator.hasNext()) {
        Ticket ticket = iterator.next();
        if (ticket.getPrice() > priceThreshold) {
          iterator.remove();
          removedCount++;
        }
      }

      System.out.println(
          TextColor.ANSI_GREEN + "Удалено билетов: " + removedCount + TextColor.ANSI_RESET);
    } catch (NumberFormatException e) {
      System.out.println(
          TextColor.ANSI_RED + "Неверный формат числа: " + args[0] + TextColor.ANSI_RESET);
    }
  }
}
