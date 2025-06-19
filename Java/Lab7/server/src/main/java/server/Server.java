package server;

import common.managers.ServerCommandManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import server.collectionManagers.SqlCollectionManager;
import server.userManagers.SqlUserManager;

public final class Server {
  private Server() {
    throw new UnsupportedOperationException("У этого класса не может быть сущностей");
  }

  public static void main(String[] args) throws IOException {
    // Считываем порт
    if (args.length < 1) {
      System.err.println("Ошибка: нужно передать порт в качестве аргумента.");
      System.err.println("Использование: java -jar server.jar <port>");
      System.exit(1);
    }

    int port;
    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.err.println("Ошибка: порт должен быть целым числом.");
      System.exit(1);
      return;
    }

    String dbHost = System.getenv("DB_HOST");
    String dbName = System.getenv("DB_NAME");
    String dbUser = System.getenv("DB_USER");
    String dbPassword = System.getenv("DB_PASSWORD");

    if (dbHost == null || dbName == null || dbUser == null || dbPassword == null) {
      System.err.println(
          "Ошибка: необходимо задать переменные окружения DB_HOST, DB_NAME, DB_USER, DB_PASSWORD.");
      System.exit(1);
    }

    try {
      Connection connection =
          DriverManager.getConnection(
              "jdbc:postgresql://" + dbHost + "/" + dbName, dbUser, dbPassword);

      SqlUserManager userManager = new SqlUserManager(connection);
      SqlCollectionManager collectionManager = new SqlCollectionManager(connection);
      collectionManager.initTables();

      ServerCommandManager commandManager =
          new ServerCommandManager(collectionManager, userManager);
      ServerInstance server = new ServerInstance(commandManager, collectionManager, userManager);
      server.run(port);

    } catch (SQLException e) {
      System.err.println("Не удалось подключиться к базе данных: " + e.getMessage());
      System.exit(1);
    }
  }
}
