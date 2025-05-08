package managers.commands;

import data.Ticket;
import data.Venue;
import data.generators.VenueGenerator;
import java.util.Iterator;
import managers.CollectionManager;

/**
 * Класс команды которая удаляет один элемент с определённым venue
 *
 * @author Maks
 * @version 1.0
 */
public class RemoveAnyByVenueCommand implements Command {
  private final CollectionManager collectionManager;

  public RemoveAnyByVenueCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public void execute(String[] args) {

    Venue inputVenue = VenueGenerator.createVenue();

    Iterator<Ticket> iterator = collectionManager.getDequeCollection().iterator();
    while (iterator.hasNext()) {
      Ticket ticket = iterator.next();
      Venue venue = ticket.getVenue();

      if (venue != null && venue.equals(inputVenue)) {
        iterator.remove();
        System.out.println("Билет с указанным venue удалён.");
        return;
      }
    }

    System.out.println("Билет с таким venue не найден.");
  }

  @Override
  public String getDescription() {
    return "удаляет один билет с эквивалентным Venue";
  }
}
