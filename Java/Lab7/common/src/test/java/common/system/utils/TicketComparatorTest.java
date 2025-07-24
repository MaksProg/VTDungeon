package common.system.utils;

import static org.junit.jupiter.api.Assertions.*;

import common.data.*;
import org.junit.jupiter.api.Test;

class TicketComparatorTest {

  private final TicketComparator comparator = new TicketComparator();

  private Ticket baseTicket() {
    Location town = new Location(1, 2L, "TownName");
    Address address = new Address("12345", town);
    Venue venue = new Venue("VenueName", 100, VenueType.BAR, address);
    return new Ticket("Name", new Coordinates(1.0, 2), 100.0, TicketType.VIP, venue);
  }

  @Test
  void testTicketsEqual() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    assertEquals(0, comparator.compare(t1, t2));
  }

  @Test
  void testCompareByName() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.setName("ZName");
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareByCoordinatesX() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getCoordinates().setX(99.0);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareByCoordinatesY() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getCoordinates().setY(999);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareByPrice() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.setPrice(999.0);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareByType() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.setType(TicketType.USUAL);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareVenueNullVsNotNull() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.setVenue(null);
    assertTrue(comparator.compare(t1, t2) > 0);
  }

  @Test
  void testCompareByVenueName() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().setName("ZZZ");
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareByVenueCapacity() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().setCapacity(999);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareByVenueType() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().setType(VenueType.STADIUM);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareAddressNullVsNotNull() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().setAddress(null);
    assertTrue(comparator.compare(t1, t2) > 0);
  }

  @Test
  void testCompareByZipCode() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().getAddress().setZipCode("ZZZ");
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareTownX() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().getAddress().getTown().setX(99);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareTownY() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().getAddress().getTown().setY(999);
    assertTrue(comparator.compare(t1, t2) < 0);
  }

  @Test
  void testCompareTownNameNullVsNotNull() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().getAddress().getTown().setName(null);
    assertTrue(comparator.compare(t1, t2) > 0);
  }

  @Test
  void testCompareTownNameValue() {
    Ticket t1 = baseTicket();
    Ticket t2 = baseTicket();
    t2.getVenue().getAddress().getTown().setName("ZZZ");
    assertTrue(comparator.compare(t1, t2) < 0);
  }
}
