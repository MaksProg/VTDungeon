package server.collectionManagers;

import static org.junit.jupiter.api.Assertions.*;

import common.data.*;
import java.sql.*;
import java.util.Deque;
import org.junit.jupiter.api.*;

public class SqlCollectionManagerTest {
  private Connection conn;
  private SqlCollectionManager collectionManager;

  @BeforeEach
  void setup() throws Exception {
    conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
    try (Statement stmt = conn.createStatement()) {
      stmt.executeUpdate("DROP TABLE IF EXISTS tickets");
      stmt.executeUpdate("DROP TABLE IF EXISTS venues");
      stmt.executeUpdate("DROP TABLE IF EXISTS users");

      stmt.executeUpdate(
          "CREATE TABLE users (id SERIAL PRIMARY KEY, username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, salt TEXT NOT NULL)");
    }
    try (PreparedStatement ps =
        conn.prepareStatement("INSERT INTO users (username, password, salt) VALUES (?, ?, ?)")) {
      ps.setString(1, "testuser");
      ps.setString(2, "dummyhash");
      ps.setString(3, "dummysalt");
      ps.executeUpdate();
    }

    collectionManager = new SqlCollectionManager(conn);
    collectionManager.initTables();
  }

  @AfterEach
  void cleanup() throws Exception {
    conn.close();
  }

  Ticket createSampleTicket(String name) {
    Ticket ticket = new Ticket();
    ticket.setName(name);
    ticket.setCoordinates(new Coordinates(1.1, 1));
    ticket.setPrice(100.0);
    ticket.setType(TicketType.VIP);
    ticket.setOwnerUsername("testuser");

    Location loc = new Location(1, 2L, "Town");
    Address address = new Address("12345", loc);
    Venue venue = new Venue("Venue", 1000, VenueType.BAR, address);
    ticket.setVenue(venue);

    return ticket;
  }

  @Test
  void testAddTicketAndGetDequeCollection() throws Exception {
    Ticket ticket = createSampleTicket("Ticket 1");
    collectionManager.addTicket(ticket);

    Deque<Ticket> tickets = collectionManager.getDequeCollection();
    assertEquals(1, tickets.size());
    assertEquals("Ticket 1", tickets.getFirst().getName());
  }

  @Test
  void testClearCollection() throws Exception {
    collectionManager.addTicket(createSampleTicket("To Clear"));
    int deleted = collectionManager.clearCollection("testuser");
    assertEquals(1, deleted);
    assertTrue(collectionManager.getDequeCollection().isEmpty());
  }

  @Test
  void testGetVenueById() throws Exception {
    Ticket ticket = createSampleTicket("With Venue");
    collectionManager.addTicket(ticket);
    Venue venue = ticket.getVenue();
    assertNotNull(venue.getId());
    Venue retrieved = collectionManager.getVenueById(venue.getId());
    assertNotNull(retrieved);
    assertEquals(venue.getName(), retrieved.getName());
  }

  @Test
  void testRemoveById() throws Exception {
    Ticket ticket = createSampleTicket("To Remove");
    collectionManager.addTicket(ticket);
    boolean removed = collectionManager.removeById(ticket.getId(), "testuser");
    assertTrue(removed);
    assertTrue(collectionManager.getDequeCollection().isEmpty());
  }

  @Test
  void testUpdateTicketById() throws Exception {
    Ticket ticket = createSampleTicket("To Update");
    collectionManager.addTicket(ticket);
    Ticket updated = createSampleTicket("Updated Name");
    updated.setCoordinates(new Coordinates(2.2, 2));
    updated.setPrice(200.0);

    boolean result = collectionManager.updateTicketById(ticket.getId(), updated, "testuser");
    assertTrue(result);
    Ticket resultTicket = collectionManager.getTicketById(ticket.getId());
    assertEquals("Updated Name", resultTicket.getName());
    assertEquals(2.2, resultTicket.getCoordinates().getX());
  }

  @Test
  void testGetTicketById() throws Exception {
    Ticket ticket = createSampleTicket("FindMe");
    collectionManager.addTicket(ticket);
    Ticket found = collectionManager.getTicketById(ticket.getId());
    assertNotNull(found);
    assertEquals("FindMe", found.getName());
  }

  @Test
  void testGetMaxTicket() throws Exception {
    Ticket t1 = createSampleTicket("AAA");
    t1.setPrice(50.0);
    collectionManager.addTicket(t1);

    Ticket t2 = createSampleTicket("ZZZ");
    t2.setPrice(150.0);
    collectionManager.addTicket(t2);

    Ticket max = collectionManager.getMaxTicket();
    assertNotNull(max);
    assertEquals("ZZZ", max.getName());
  }

  @Test
  void testMapRowToTicket() throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    Statement stmt = conn.createStatement();

    stmt.execute(
        """
        CREATE TABLE users (username TEXT PRIMARY KEY);
        CREATE TABLE venues (
            id SERIAL PRIMARY KEY,
            name TEXT NOT NULL,
            capacity INTEGER,
            type TEXT NOT NULL,
            zip_code TEXT NOT NULL,
            x BIGINT NOT NULL,
            y BIGINT NOT NULL,
            town_name TEXT
        );
        CREATE TABLE tickets (
            id SERIAL PRIMARY KEY,
            name TEXT NOT NULL,
            coordinate_x DOUBLE PRECISION NOT NULL,
            coordinate_y INTEGER,
            creation_date TIMESTAMP NOT NULL,
            price DOUBLE PRECISION NOT NULL,
            type TEXT,
            owner_username TEXT NOT NULL,
            venue_id INTEGER,
            FOREIGN KEY (owner_username) REFERENCES users(username),
            FOREIGN KEY (venue_id) REFERENCES venues(id)
        );
    """);

    stmt.execute("INSERT INTO users VALUES ('test_user')");
    stmt.execute(
        """
        INSERT INTO venues (name, capacity, type, zip_code, x, y, town_name)
        VALUES ('Venue1', 100, 'BAR', '12345', 10, 20, 'Townsville')
    """);
    stmt.execute(
        """
        INSERT INTO tickets (name, coordinate_x, coordinate_y, creation_date, price, type, owner_username, venue_id)
        VALUES ('Ticket1', 1.1, 2, NOW(), 99.99, 'VIP', 'test_user', 1)
    """);

    PreparedStatement ps =
        conn.prepareStatement(
            """
        SELECT t.*, v.id AS venue_id, v.name AS venue_name, v.capacity, v.type AS venue_type,
               v.zip_code, v.x AS venue_x, v.y AS venue_y, v.town_name
        FROM tickets t
        LEFT JOIN venues v ON t.venue_id = v.id
    """);

    ResultSet rs = ps.executeQuery();

    assertTrue(rs.next());

    SqlCollectionManager manager = new SqlCollectionManager(conn);
    Ticket ticket = manager.mapRowToTicket(rs);

    assertNotNull(ticket);
    assertEquals("Ticket1", ticket.getName());
    assertEquals(1.1, ticket.getCoordinates().getX());
    assertEquals(2, ticket.getCoordinates().getY());
    assertEquals("test_user", ticket.getOwnerUsername());
    assertNotNull(ticket.getVenue());
    assertEquals("Venue1", ticket.getVenue().getName());
    assertEquals("Townsville", ticket.getVenue().getTown().getName());
  }
}
