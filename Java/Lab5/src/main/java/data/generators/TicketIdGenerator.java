package data.generators;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс отвечающий за генерацию поля ID в Ticket
 *
 * @author Maks
 * @version 1.0
 */
public class TicketIdGenerator {
  private static AtomicInteger id = new AtomicInteger(1);

  public static int getId() {
    return id.getAndIncrement();
  }
}
