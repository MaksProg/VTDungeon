package client;

import common.data.Ticket;
import common.data.Venue;
import common.managers.CollectionManager;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

public class EmptyCollectionManager implements CollectionManager {
  @Override
  public Deque<Ticket> getDequeCollection() {
    return new ArrayDeque<>();
  }

  @Override
  public ZonedDateTime getInitDate() {
    return ZonedDateTime.now();
  }

  @Override
  public int clearCollection(String username) {
    return 0;
  }

  @Override
  public void addTicket(Ticket ticket) {}

  @Override
  public boolean removeById(int id, String username) {
    return false;
  }

  @Override
  public boolean updateTicketById(int id, Ticket updatedTicket, String username) {
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
}
