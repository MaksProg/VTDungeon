package common.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LocationTest {

  @Test
  void testGettersAndSetters() {
    Location location = new Location();
    location.setX(42L);
    location.setY(73L);
    location.setName("Central Park");

    assertEquals(42L, location.getX());
    assertEquals(73L, location.getY());
    assertEquals("Central Park", location.getName());
  }

  @Test
  void testConstructorAndToString() {
    Location location = new Location(10L, 20L, "Mountain");
    assertEquals(10L, location.getX());
    assertEquals(20L, location.getY());
    assertEquals("Mountain", location.getName());

    String str = location.toString();
    assertTrue(str.contains("x=10"));
    assertTrue(str.contains("y=20"));
    assertTrue(str.contains("Mountain"));
  }

  @Test
  void testEqualsAndHashCode() {
    Location l1 = new Location(1L, 2L, "Place");
    Location l2 = new Location(1L, 2L, "Other");

    // equals проверяет только x и y
    assertEquals(l1, l2);
    assertEquals(l1.hashCode(), l2.hashCode());
  }

  @Test
  void testNotEqual() {
    Location l1 = new Location(1L, 2L, "A");
    Location l2 = new Location(1L, 999L, "A");

    assertNotEquals(l1, l2);
  }
}
