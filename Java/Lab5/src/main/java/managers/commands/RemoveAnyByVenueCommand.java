package managers.commands;

import data.Ticket;
import data.Venue;
import java.util.Iterator;
import managers.CollectionManager;

/**
 * Класс команды которая удаляет один элемент с определённым venue
 *
 * @author Maks
 * @version 1.0
 */
public class RemoveAnyByVenueCommand implements Command {
  @Override
  public void execute(String[] args) {
    if (args.length < 1) {
      System.out.println(
          "Ошибка: не указано название venue. Использование: remove_any_by_venue {venue}");
      return;
    }

    String venueName = args[0];

    Iterator<Ticket> iterator = CollectionManager.getDequeCollection().iterator();
    while (iterator.hasNext()) {
      Ticket ticket = iterator.next();
      Venue venue = ticket.getVenue();

      if (venue != null && venue.getName().equals(venueName)) {
        iterator.remove();
        System.out.println("Билет с venue '" + venueName + "' удалён.");
        return;
      }
    }

    System.out.println("Билет с venue '" + venueName + "' не найден.");
  }
}
