package client;

import static org.junit.jupiter.api.Assertions.*;

import common.data.Ticket;
import common.data.Venue;
import java.time.ZonedDateTime;
import java.util.Deque;
import org.junit.jupiter.api.Test;

public class EmptyCollectionManagerTest {

  @Test
  public void testGetDequeCollectionReturnsEmpty() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    Deque<Ticket> deque = manager.getDequeCollection();
    assertNotNull(deque);
    assertTrue(deque.isEmpty(), "Коллекция должна быть пустой");
  }

  @Test
  public void testGetInitDateReturnsNonNull() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    ZonedDateTime initDate = manager.getInitDate();
    assertNotNull(initDate);
  }

  @Test
  public void testClearCollectionReturnsZero() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    int cleared = manager.clearCollection("anyUser");
    assertEquals(0, cleared);
  }

  @Test
  public void testAddTicketDoesNothing() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    manager.addTicket(new Ticket());
  }

  @Test
  public void testRemoveByIdReturnsFalse() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    boolean removed = manager.removeById(1, "user");
    assertFalse(removed);
  }

  @Test
  public void testUpdateTicketByIdReturnsFalse() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    boolean updated = manager.updateTicketById(1, new Ticket(), "user");
    assertFalse(updated);
  }

  @Test
  public void testGetTicketByIdReturnsNull() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    Ticket ticket = manager.getTicketById(1);
    assertNull(ticket);
  }

  @Test
  public void testGetVenueByIdReturnsNull() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    Venue venue = manager.getVenueById(1);
    assertNull(venue);
  }

  @Test
  public void testGetMaxTicketReturnsNull() {
    EmptyCollectionManager manager = new EmptyCollectionManager();
    Ticket maxTicket = manager.getMaxTicket();
    assertNull(maxTicket);
  }
}
