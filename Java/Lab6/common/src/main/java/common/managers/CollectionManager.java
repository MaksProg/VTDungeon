package common.managers;

import common.data.Ticket;
import java.time.ZonedDateTime;
import java.util.Deque;

/**
 * Интерфейс, который указывает функции нужные для управления коллекцией
 *
 * @author Maks
 * @version 1.0
 */
public interface CollectionManager {
  Deque<Ticket> getDequeCollection();

  ZonedDateTime initDate = null;

  ZonedDateTime getInitDate();

  void clearCollection();

  void setDequeCollection(Deque<Ticket> newCollection);

  void addTicket(Ticket ticket);

  boolean removeById(int id);

  Ticket getMaxTicket();
}
