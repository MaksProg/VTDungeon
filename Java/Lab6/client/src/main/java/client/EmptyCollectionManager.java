package client;

import common.data.Ticket;
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
  public void clearCollection() {}

  @Override
  public void setDequeCollection(Deque<Ticket> newCollection) {}

  @Override
  public void addTicket(Ticket ticket) {}

  @Override
  public boolean removeById(int id) {
    return false;
  }

  @Override
  public Ticket getMaxTicket() {
    return null;
  }
}
