package managers;

import data.Venue;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс реализующий работу с Venue
 *
 * @author Maks
 * @version 1.0
 */
public class VenueManager {
  private final Map<Integer, Venue> venues = new HashMap<>();

  public void addVenue(Venue venue) {
    if (!venues.containsKey(venue.getId())) {
      venues.put(venue.getId(), venue);
    }
  }

  public Venue getById(Integer id) {
    return venues.get(id);
  }

  public Venue getByName(String name) {
    return venues.values().stream()
        .filter(v -> v.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }
}
