package managers.commands;

import data.Venue;
import data.generators.VenueGenerator;
import managers.VenueManager;

/**
 * Добавляет новое Venue
 *
 * @author Maks
 * @version 1.0
 */
public class AddVenueCommand implements Command {
  private final VenueManager venueManager;

  public AddVenueCommand(VenueManager venueManager) {
    this.venueManager = venueManager;
  }

  @Override
  public void execute(String[] args) {
    Venue venue = VenueGenerator.createVenue();
    venueManager.addVenue(venue);
    System.out.println("Площадка добавлена с ID: " + venue.getId());
  }

  @Override
  public String getDescription() {
    return "добавляет новую площадку в список";
  }
}
