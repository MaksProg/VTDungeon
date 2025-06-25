package common.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VenueTest {

  @Test
  void testSettersAndGetters() {
    Venue venue = new Venue();

    venue.setId(10);
    venue.setName("Test Venue");
    venue.setCapacity(1000);
    venue.setType(VenueType.STADIUM);

    Address address = new Address("12345", new Location(1L, 2L, "TownName"));
    venue.setAddress(address);

    assertEquals(10, venue.getId());
    assertEquals("Test Venue", venue.getName());
    assertEquals(1000, venue.getCapacity());
    assertEquals(VenueType.STADIUM, venue.getType());
    assertEquals(address, venue.getAddress());
  }

  @Test
  void testGetTownAndZipCode() {
    Location location = new Location(5L, 10L, "MyTown");
    Address address = new Address("54321", location);
    Venue venue = new Venue("Name", 500, VenueType.STADIUM, address);

    assertEquals(location, venue.getTown());
    assertEquals("54321", venue.getZipCode());
  }

  @Test
  void testToStringContainsAllFields() {
    Address address = new Address("zip", new Location(7L, 8L, "Town"));
    Venue venue = new Venue("VenueName", 200, VenueType.STADIUM, address);
    venue.setId(99);

    String str = venue.toString();
    assertTrue(str.contains("99"));
    assertTrue(str.contains("VenueName"));
    assertTrue(str.contains("200"));
    assertTrue(str.contains("STADIUM"));
    assertTrue(str.contains("zip"));
    assertTrue(str.contains("Town"));
  }

  @Test
  void testEqualsAndHashCode() {
    Address address1 = new Address("zip1", new Location(1L, 1L, "Town1"));
    Address address2 = new Address("zip1", new Location(1L, 1L, "Town1"));

    Venue v1 = new Venue("Name", 10, VenueType.STADIUM, address1);
    Venue v2 = new Venue("Name", 10, VenueType.STADIUM, address2);

    v1.setId(1);
    v2.setId(2);

    assertEquals(v1, v2);
    assertEquals(v1.hashCode(), v2.hashCode());

    Venue v3 = new Venue("Name", 11, VenueType.STADIUM, address1);
    assertNotEquals(v1, v3);
  }
}
