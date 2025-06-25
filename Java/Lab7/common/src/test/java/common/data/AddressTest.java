package common.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  void testGettersAndSetters() {
    Location location = new Location(3L, 4L, "City");
    Address address = new Address();
    address.setZipCode("12345");

    try {
      var townField = Address.class.getDeclaredField("town");
      townField.setAccessible(true);
      townField.set(address, location);
    } catch (Exception e) {
      fail("Не удалось установить поле town");
    }

    assertEquals("12345", address.getZipCode());
    assertEquals(location, address.getTown());
  }

  @Test
  void testConstructorAndToString() {
    Location location = new Location(1L, 2L, "Townsville");
    Address address = new Address("54321", location);

    assertEquals("54321", address.getZipCode());
    assertEquals(location, address.getTown());

    String str = address.toString();
    assertTrue(str.contains("54321"));
    assertTrue(str.contains("Townsville"));
  }

  @Test
  void testEqualsAndHashCode() {
    Location loc1 = new Location(1L, 2L, "A");
    Location loc2 = new Location(1L, 2L, "B");
    Address a1 = new Address("000", loc1);
    Address a2 = new Address("000", loc2);

    assertEquals(a1, a2);
    assertEquals(a1.hashCode(), a2.hashCode());
  }

  @Test
  void testNotEqual() {
    Address a1 = new Address("111", new Location(1L, 2L, "A"));
    Address a2 = new Address("222", new Location(1L, 2L, "A"));

    assertNotEquals(a1, a2); // zipCode разный
  }
}
