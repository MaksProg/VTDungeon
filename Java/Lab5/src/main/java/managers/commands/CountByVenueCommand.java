package managers.commands;

import data.Ticket;
import data.Venue;
import data.generators.VenueGenerator;
import managers.CollectionManager;

/**
 * Класс команды которая считает количество билетов с одинаковым Venue
 *
 * @author Maks
 * @version 1.0
 */
public class CountByVenueCommand implements Command {
  private final CollectionManager collectionManager;

  public CountByVenueCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public void execute(String[] args) {

    Venue inputVenue = VenueGenerator.createVenue();

    long count =
        collectionManager.getDequeCollection().stream()
            .map(Ticket::getVenue)
            .filter(venue -> venue != null && venue.equals(inputVenue))
            .count();

    System.out.println("Количество билетов с введённой площадкой: " + count);
  }

  @Override
  public String getDescription() {
    return "считает количество билетов с одинаковым Venue";
  }
}
