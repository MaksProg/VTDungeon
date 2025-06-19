package server.collectionManagers;

import common.data.*;
import common.managers.CollectionManager;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

public class SqlCollectionManager implements CollectionManager {
  private static final String CREATE_TABLE_QUERY =
      "CREATE TABLE IF NOT EXISTS venues ("
          + "id SERIAL PRIMARY KEY,"
          + "name TEXT NOT NULL,"
          + "capacity INTEGER CHECK (capacity > 0),"
          + "type TEXT NOT NULL CHECK (type IN ('BAR', 'LOFT', 'PUB', 'CINEMA', 'STADIUM')),"
          + "zip_code TEXT NOT NULL CHECK (zip_code > 0),"
          + "x BIGINT NOT NULL,"
          + "y BIGINT NOT NULL,"
          + "town_name TEXT"
          + ");"
          + "CREATE TABLE IF NOT EXISTS tickets("
          + "id serial PRIMARY KEY,"
          + "name text NOT NULL,"
          + "coordinate_x DOUBLE PRECISION NOT NULL,"
          + "coordinate_y INTEGER CHECK(coordinate_y > -593),"
          + "creation_date TIMESTAMP NOT NULL,"
          + "price DOUBLE PRECISION NOT NULL CHECK(price > 0),"
          + "type text CHECK(type IN('VIP','USUAL','CHEAP','BUDGETARY')),"
          + "owner_username TEXT NOT NULL,"
          + "venue_id INTEGER,"
          + "CONSTRAINT fk_owner FOREIGN KEY (owner_username) REFERENCES users(username) ON DELETE CASCADE,"
          + "CONSTRAINT fk_venue FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE SET NULL)";

  private static final String SELECT_TICKETS_QUERY =
      "SELECT t.id, t.name, t.coordinate_x, t.coordinate_y, t.creation_date, "
          + "t.price, t.type, t.owner_username, u.username AS owner_login, "
          + "v.id AS venue_id, v.name AS venue_name, v.capacity, v.type AS venue_type, "
          + "v.zip_code, v.x AS venue_x, v.y AS venue_y, v.town_name "
          + "FROM tickets AS t "
          + "LEFT JOIN users AS u ON t.owner_username = u.username "
          + "LEFT JOIN venues AS v ON t.venue_id = v.id";

  private static final Logger logger = Logger.getLogger(SqlCollectionManager.class.getName());
  private final Connection conn;
  private final Deque<Ticket> collection = new ConcurrentLinkedDeque<>();

  public SqlCollectionManager(Connection conn) {
    this.conn = conn;
  }

  public void initTables() throws SQLException {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(CREATE_TABLE_QUERY);
    }

    try (PreparedStatement ps = conn.prepareStatement(SELECT_TICKETS_QUERY);
        ResultSet rs = ps.executeQuery()) {

      int invalidTickets = 0;

      while (rs.next()) {
        Ticket ticket = mapRowToTicket(rs);
        if (ticket != null) {
          collection.add(ticket);
        } else {
          invalidTickets++;
        }
      }
      logger.info(
          "Loaded "
              + collection.size()
              + " tickets, removed "
              + invalidTickets
              + " invalid tickets.");
    }
  }

  private Ticket mapRowToTicket(ResultSet rs) {
    try {
      int id = rs.getInt("id");
      String name = rs.getString("name");
      double coordX = rs.getDouble("coordinate_x");
      int coordY = rs.getInt("coordinate_y");
      ZonedDateTime creationDate =
          rs.getTimestamp("creation_date").toInstant().atZone(ZoneId.systemDefault());
      double price = rs.getDouble("price");
      String typeStr = rs.getString("type");
      TicketType type = TicketType.valueOf(typeStr);

      String ownerUsername = rs.getString("owner_username");

      Coordinates coordinates = new Coordinates(coordX, coordY);

      int venueId = rs.getInt("venue_id");
      Venue venue = null;
      if (!rs.wasNull()) {
        String venueName = rs.getString("venue_name");
        int capacity = rs.getInt("capacity");
        String venueTypeStr = rs.getString("venue_type");
        VenueType venueType = VenueType.valueOf(venueTypeStr);

        int zipCode = rs.getInt("zip_code");
        long x = rs.getLong("venue_x"); // изменено с "x" на "venue_x"
        long y = rs.getLong("venue_y"); // изменено с "y" на "venue_y"
        String townName = rs.getString("town_name");

        Location town = new Location(x, y, townName);
        Address address = new Address(String.valueOf(zipCode), town);

        venue = new Venue(venueName, capacity, venueType, address);
        venue.setId(venueId);
      }

      Ticket ticket = new Ticket();
      ticket.setId(id);
      ticket.setName(name);
      ticket.setCoordinates(coordinates);
      ticket.setCreationDate(creationDate);
      ticket.setPrice(price);
      ticket.setType(type);
      ticket.setOwnerUsername(ownerUsername);
      ticket.setVenue(venue);

      return ticket;

    } catch (Exception e) {
      logger.warning("Найдена некорректная информация: " + e.getMessage());
      return null;
    }
  }

  public static void prepareVenueStatement(PreparedStatement s, Venue venue, int paramOffset)
      throws SQLException {
    s.setString(1 + paramOffset, venue.getName());

    if (venue.getCapacity() != null) {
      s.setInt(2 + paramOffset, venue.getCapacity());
    } else {
      s.setNull(2 + paramOffset, Types.INTEGER);
    }

    s.setString(3 + paramOffset, venue.getType().toString()); // enum to string

    s.setString(4 + paramOffset, venue.getZipCode());
    s.setLong(5 + paramOffset, venue.getTown().getX());
    s.setLong(6 + paramOffset, venue.getTown().getY());

    if (venue.getTown().getName() != null) {
      s.setString(7 + paramOffset, venue.getTown().getName());
    } else {
      s.setNull(7 + paramOffset, Types.VARCHAR);
    }
  }

  public static void prepareTicketStatement(PreparedStatement s, Ticket ticket, int paramOffset)
      throws SQLException {
    s.setString(1 + paramOffset, ticket.getName());
    s.setDouble(2 + paramOffset, ticket.getCoordinates().getX());
    s.setInt(3 + paramOffset, ticket.getCoordinates().getY());
    s.setTimestamp(4 + paramOffset, Timestamp.valueOf(ticket.getCreationDate().toLocalDateTime()));
    s.setDouble(5 + paramOffset, ticket.getPrice());

    if (ticket.getType() != null) {
      s.setString(6 + paramOffset, ticket.getType().toString());
    } else {
      s.setNull(6 + paramOffset, Types.VARCHAR);
    }

    s.setString(7 + paramOffset, ticket.getOwnerUsername());

    if (ticket.getVenue() != null) {
      s.setLong(8 + paramOffset, ticket.getVenue().getId());
    } else {
      s.setNull(8 + paramOffset, Types.BIGINT);
    }
  }

  private Integer findVenueId(Venue venue) throws SQLException {
    String query =
        "SELECT id FROM venues WHERE name = ? AND capacity = ? AND type = ? AND zip_code = ? AND x = ? AND y = ? AND town_name = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, venue.getName());
      stmt.setInt(2, venue.getCapacity());
      stmt.setString(3, venue.getType().name());
      stmt.setString(4, venue.getZipCode());
      stmt.setLong(5, venue.getTown().getX());
      stmt.setLong(6, venue.getTown().getY());
      stmt.setString(7, venue.getTown().getName());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("id");
        }
      }
    }
    return null;
  }

  @Override
  public Venue getVenueById(int id) {
    String query =
        """
        SELECT * FROM venues WHERE id = ?
    """;

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String name = rs.getString("name");
        Integer capacity = rs.getInt("capacity");
        String typeStr = rs.getString("type");
        VenueType type = typeStr != null ? VenueType.valueOf(typeStr) : null;

        String zipCode = rs.getString("zip_code");
        long x = rs.getLong("x");
        long y = rs.getLong("y");
        String townName = rs.getString("town_name");

        Address address = new Address(zipCode, new Location(x, y, townName));
        Venue venue = new Venue(name, capacity, type, address);
        venue.setId(id);
        return venue;
      }
    } catch (SQLException e) {
      logger.warning("Ошибка при получении Venue: " + e.getMessage());
    }
    return null;
  }

  @Override
  public Deque<Ticket> getDequeCollection() {
    return new ArrayDeque<>(collection);
  }

  @Override
  public ZonedDateTime getInitDate() {
    return initDate;
  }

  @Override
  public int clearCollection(String username) {
    String deleteSQL = "DELETE FROM tickets WHERE owner_username = ?";
    int deleted = 0;

    try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
      ps.setString(1, username);
      deleted = ps.executeUpdate();

      collection.removeIf(ticket -> username.equals(ticket.getOwnerUsername()));
    } catch (SQLException e) {
      logger.warning(
          "Ошибка при удалении билетов пользователя '" + username + "': " + e.getMessage());
    }

    return deleted;
  }

  @Override
  public void addTicket(Ticket ticket) throws SQLException {
    Venue venue = ticket.getVenue();
    Integer venueId = null;

    if (venue != null && (venue.getName() == null || venue.getType() == null)) {
      Venue fullVenue = getVenueById(venue.getId());
      if (fullVenue == null) {
        logger.warning("Площадка с id " + venue.getId() + " не найдена.");
        throw new SQLException("Площадка с id " + venue.getId() + " не найдена.");
      }
      venue = fullVenue;
      ticket.setVenue(venue);
      venueId = venue.getId();
    } else if (venue != null && (venue.getId() == null || venue.getId() == 0)) {
      venueId = insertOrGetVenueId(venue);
      venue.setId(venueId);
    } else if (venue != null) {
      venueId = venue.getId();
    }

    String insertTicketSQL =
        "INSERT INTO tickets "
            + "(name, coordinate_x, coordinate_y, creation_date, price, type, owner_username, venue_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

    try (PreparedStatement ticketStmt = conn.prepareStatement(insertTicketSQL)) {
      ticket.setCreationDate(ZonedDateTime.now());
      prepareTicketStatement(ticketStmt, ticket, 0);

      if (venueId != null) {
        ticketStmt.setInt(8, venueId);
      } else {
        ticketStmt.setNull(8, Types.INTEGER);
      }

      try (ResultSet rs = ticketStmt.executeQuery()) {
        if (rs.next()) {
          int ticketId = rs.getInt("id");
          ticket.setId(ticketId);
        }
      }
    }

    collection.add(ticket);
    logger.info("Билет успешно добавлен: ID = " + ticket.getId());
  }

  private int insertOrGetVenueId(Venue venue) throws SQLException {
    Integer existingId = findVenueId(venue);
    if (existingId != null) {
      venue.setId(existingId);
      return existingId;
    }

    String insertVenueSQL =
        "INSERT INTO venues (name, capacity, type, zip_code, x, y, town_name) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
    try (PreparedStatement stmt = conn.prepareStatement(insertVenueSQL)) {
      prepareVenueStatement(stmt, venue, 0);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          int newId = rs.getInt("id");
          venue.setId(newId);
          return newId;
        } else {
          throw new SQLException("Failed to insert venue");
        }
      }
    }
  }

  @Override
  public boolean removeById(int id, String username) {
    String query = "DELETE FROM tickets WHERE id = ? AND owner_username = ?";

    try (PreparedStatement s = conn.prepareStatement(query)) {
      s.setInt(1, id);
      s.setString(2, username);
      int affectedRows = s.executeUpdate();
      if (affectedRows > 0) {
        return collection.removeIf(x -> x.getId() == id && username.equals(x.getOwnerUsername()));
      }
      return false;
    } catch (SQLException e) {
      logger.warning("Не получилось удалить билет: " + e.getMessage());
      return false;
    }
  }

  @Override
  public boolean updateTicketById(int id, Ticket updatedTicket, String username) {
    String checkOwnershipQuery = "SELECT owner_username FROM tickets WHERE id = ?";
    String updateQuery =
        """
        UPDATE tickets
        SET name = ?, coordinate_x = ?, coordinate_y = ?, price = ?,
            type = ?, venue_id = ?
        WHERE id = ? AND owner_username = ?
    """;

    try (PreparedStatement checkStmt = conn.prepareStatement(checkOwnershipQuery)) {
      checkStmt.setInt(1, id);
      ResultSet rs = checkStmt.executeQuery();

      if (!rs.next()) {
        return false; // Билета с таким id нет
      }

      String owner = rs.getString("owner_username");
      if (!owner.equals(username)) {
        return false; // Пользователь не владелец
      }

      Venue venue = updatedTicket.getVenue();
      Integer venueId = null;

      if (venue != null) {
        // Если venue с ID, но без остальных данных — подгружаем из БД
        if ((venue.getName() == null || venue.getType() == null)
            && venue.getId() != null
            && venue.getId() != 0) {
          Venue fullVenue = getVenueById(venue.getId());
          if (fullVenue == null) {
            logger.warning("Площадка с id " + venue.getId() + " не найдена.");
            return false;
          }
          venue = fullVenue;
          updatedTicket.setVenue(venue);
          venueId = venue.getId();
        } else if (venue.getId() == null || venue.getId() == 0) {
          venueId = insertOrGetVenueId(venue);
          venue.setId(venueId);
        } else {
          venueId = venue.getId();
        }
      }

      try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
        updateStmt.setString(1, updatedTicket.getName());
        updateStmt.setDouble(2, updatedTicket.getCoordinates().getX());
        updateStmt.setInt(3, updatedTicket.getCoordinates().getY());
        updateStmt.setDouble(4, updatedTicket.getPrice());

        if (updatedTicket.getType() != null) {
          updateStmt.setString(5, updatedTicket.getType().toString());
        } else {
          updateStmt.setNull(5, Types.VARCHAR);
        }

        if (venueId != null) {
          updateStmt.setInt(6, venueId);
        } else {
          updateStmt.setNull(6, Types.INTEGER);
        }

        updateStmt.setInt(7, id);
        updateStmt.setString(8, username);

        int updatedRows = updateStmt.executeUpdate();

        if (updatedRows > 0) {
          Ticket ticketInMemory = getTicketById(id);
          if (ticketInMemory != null) {
            ticketInMemory.setName(updatedTicket.getName());
            ticketInMemory.setCoordinates(updatedTicket.getCoordinates());
            ticketInMemory.setPrice(updatedTicket.getPrice());
            ticketInMemory.setType(updatedTicket.getType());
            ticketInMemory.setVenue(updatedTicket.getVenue());
          }
          return true;
        } else {
          return false;
        }
      }

    } catch (SQLException e) {
      logger.warning("Ошибка при обновлении билета: " + e.getMessage());
      return false;
    }
  }

  public Ticket getTicketById(int id) {
    for (Ticket ticket : collection) {
      if (ticket.getId() == id) {
        return ticket;
      }
    }
    return null;
  }

  @Override
  public Ticket getMaxTicket() {
    return collection.stream().max(Ticket::compareTo).orElse(null);
  }
}
