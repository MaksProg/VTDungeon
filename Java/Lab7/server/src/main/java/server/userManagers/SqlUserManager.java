package server.userManagers;

import common.data.auth.AuthCredentials;
import common.managers.auth.UserManager;
import common.system.utils.SaltUtils;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Base64;
import java.util.logging.Logger;

public class SqlUserManager implements UserManager {
  private final Logger logger = Logger.getLogger(SqlUserManager.class.getName());
  private final Connection connection;

  public SqlUserManager(Connection connection) throws SQLException {
    this.connection = connection;
    createTableIfNotExists();
  }

  private void createTableIfNotExists() throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(
          """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    salt VARCHAR(64) NOT NULL
                )
            """);
    }
  }

  private String generateSalt() {
    return SaltUtils.generateSalt(32);
  }

  private String hashPassword(String password, String salt) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-224");
      byte[] hashed = md.digest((password + salt).getBytes());
      return Base64.getEncoder().encodeToString(hashed);
    } catch (Exception e) {
      throw new RuntimeException("Ошибка в хешировании:" + e);
    }
  }

  @Override
  public Integer authenticate(AuthCredentials auth) {
    try (PreparedStatement statement =
        connection.prepareStatement("SELECT id, password, salt FROM users WHERE username = ?")) {
      statement.setString(1, auth.username());
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        int userId = rs.getInt("id");
        String dbSalt = rs.getString("salt");
        String dbHash = rs.getString("password");
        String inputHash = hashPassword(auth.password(), dbSalt);
        if (dbHash.equals(inputHash)) return userId;
      }
    } catch (SQLException e) {
      logger.warning("Ошибка при аутентификации пользователя");
    }
    return null;
  }

  @Override
  public Integer register(AuthCredentials auth) {
    if (authenticate(auth) != null) return null;

    String salt = generateSalt();
    String hash = hashPassword(auth.password(), salt);

    try (PreparedStatement statement =
        connection.prepareStatement(
            "INSERT INTO users (username, password, salt) VALUES (?, ?, ?) RETURNING id")) {
      statement.setString(1, auth.username());
      statement.setString(2, hash);
      statement.setString(3, salt);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) {
      logger.warning("Ошибка при регистрации пользователя");
    }
    return null;
  }

  @Override
  public String getUsernameById(int userId) {
    try (PreparedStatement statement =
        connection.prepareStatement("SELECT username FROM users WHERE id = ?")) {
      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) return rs.getString("username");
    } catch (SQLException e) {
      logger.warning("Ошибка при попытки получения имени пользователя по его айди");
    }
    return null;
  }
}
