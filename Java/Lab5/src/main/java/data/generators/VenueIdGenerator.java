package data.generators;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс отвечающий за генерацию поля ID в Venue
 *
 * @author Maks
 * @version 1.0
 */
public class VenueIdGenerator {
  private static AtomicInteger id = new AtomicInteger(1);

  public static int getId() {
    return id.getAndIncrement();
  }
}
