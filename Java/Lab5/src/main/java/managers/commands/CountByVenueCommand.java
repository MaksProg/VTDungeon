package managers.commands;

import data.Ticket;
import managers.CollectionManager;

/**
 * Класс команды которая считает количество билетов с одинаковым VenueType
 *
 * @author Maks
 * @version 1.0
 */
public class CountByVenueCommand implements Command {
  @Override
  public void execute(String[] args) {
    if (args.length < 1) {
      System.out.println("Ошибка: укажите название площадки (venue).");
      return;
    }

    String venueName = String.join(" ", args);

    long count =
        CollectionManager.getDequeCollection().stream()
            .map(Ticket::getVenue)
            .filter(venue -> venue != null && venue.getName().equalsIgnoreCase(venueName))
            .count();

    System.out.println("Количество билетов с площадкой '" + venueName + "': " + count);
  }
}
