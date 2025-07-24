package common.managers;

import common.data.*;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

public class InMemoryCollectionManager implements CollectionManager {

  private final Deque<Ticket> tickets = new ArrayDeque<>();

  @Override
  public boolean updateTicketById(int id, Ticket updatedTicket, String username) {
    for (Ticket t : tickets) {
      if (t.getId() == id && username.equals(t.getOwnerUsername())) {
        t.setName(updatedTicket.getName());
        t.setCoordinates(updatedTicket.getCoordinates());
        t.setPrice(updatedTicket.getPrice());
        t.setType(updatedTicket.getType());
        t.setVenue(updatedTicket.getVenue());
        return true;
      }
    }
    return false;
  }

  @Override
  public Ticket getTicketById(int id) {
    return null;
  }

  @Override
  public Venue getVenueById(int id) {
    return null;
  }

  @Override
  public Ticket getMaxTicket() {
    return null;
  }

  @Override
  public Deque<Ticket> getDequeCollection() {
    return tickets;
  }

  @Override
  public ZonedDateTime getInitDate() {
    return null;
  }

  @Override
  public int clearCollection(String username) {
    return 0;
  }

  @Override
  public void addTicket(Ticket ticket) throws SQLException {}

  @Override
  public boolean removeById(int id, String username) {
    return false;
  }
}
