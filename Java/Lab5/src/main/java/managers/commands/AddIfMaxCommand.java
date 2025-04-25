package managers.commands;

import java.util.Optional;
import managers.CollectionManager;
import system.ApplicationContext;
import system.TextColor;

/**
 * Класс команды добавляющую билет, если вместимость площадки нового билета больше чем максимальная
 * вместимость среди билетов в коллекции
 *
 * @author Maks
 * @version 1.0
 */
public class AddIfMaxCommand implements Command {

  @Override
  public void execute(String[] args) {
    if (args.length == 0) {
      System.out.println("Укажите вместимость для сравнения");
      return;
    }

    try {
      int newCapacity = Integer.parseInt(args[0]);

      Optional<Integer> maxCapacity =
          CollectionManager.getDequeCollection().stream()
              .map(ticket -> ticket.getVenue().getCapacity())
              .max(Integer::compareTo);

      if (maxCapacity.isPresent()) {
        if (newCapacity > maxCapacity.get()) {
          ApplicationContext.getCommandManager().executeCommand("add");
        } else {
          System.out.println("Вместимость не превышает текущий максимум, билет не добавлен.");
        }
      } else {
        ApplicationContext.getCommandManager().executeCommand("add");
      }

    } catch (NumberFormatException e) {
      System.out.println(
          TextColor.ANSI_RED + "Ошибка: Введите корректное число" + TextColor.ANSI_RESET);
    } catch (Exception e) {
      System.out.println(TextColor.ANSI_RED + "Ошибка: " + e.getMessage() + TextColor.ANSI_RESET);
    }
  }
}
