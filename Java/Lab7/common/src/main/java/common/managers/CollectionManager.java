package common.managers;

import common.data.Ticket;
import common.data.Venue;
import java.sql.SQLException;
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

  int clearCollection(String username);

  void setDequeCollection(Deque<Ticket> newCollection);

  void addTicket(Ticket ticket) throws SQLException;

  boolean removeById(int id, String username);

  boolean updateTicketById(int id, Ticket updatedTicket, String username);

  public Ticket getTicketById(int id);

  public Venue getVenueById(int id);

  Ticket getMaxTicket();
}
