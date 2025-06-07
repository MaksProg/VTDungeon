package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import common.managers.CommandManager;
import common.managers.VenueManager;
import common.system.JSONReader;
import common.system.JSONWriter;
import java.io.IOException;
import server.collectionManager.FileCollectionManager;

public final class Server {
  private Server() {
    throw new UnsupportedOperationException("У этого класса не может быть сущностей");
  }

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.err.println("Ошибка: нужно передать путь к JSON-файлу и порт.");
      System.err.println("Использование: java -jar server.jar <название файла> <port>");
      System.exit(1);
    }

    String dataPath = args[0];
    int port;
    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Ошибка: порт должен быть целым числом.");
      System.exit(1);
      return;
    }

    VenueManager venueManager = new VenueManager();
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    JSONReader jsonReader = new JSONReader(mapper, venueManager);
    JSONWriter jsonWriter = new JSONWriter(mapper);

    FileCollectionManager collectionManager = new FileCollectionManager(dataPath, jsonReader);

    CommandManager commandManager =
        new CommandManager(collectionManager, venueManager, jsonWriter, jsonReader, dataPath);

    ServerInstance server = new ServerInstance(commandManager, collectionManager);
    server.run(port);
  }
}
