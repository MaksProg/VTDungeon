package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.managers.CollectionManager;
import common.managers.CommandManager;
import common.managers.VenueManager;
import common.system.JSONReader;
import common.system.JSONWriter;
import java.net.InetSocketAddress;

public final class Client {
  private Client() {
    throw new UnsupportedOperationException("У этого класса не должно быть сущностей.");
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Использование: java -jar client.jar <host> <port>");
      return;
    }

    try {
      String host = args[0];
      int port = Integer.parseInt(args[1]);
      InetSocketAddress addr = new InetSocketAddress(host, port);
      ObjectMapper mapper = new ObjectMapper();
      String dataPath = "";

      JSONWriter writer = new JSONWriter(mapper);
      VenueManager venueManager = new VenueManager();
      JSONReader reader = new JSONReader(mapper, venueManager);
      CollectionManager emptyCollectionManager = new EmptyCollectionManager();
      CommandManager commandManager =
          new CommandManager(emptyCollectionManager, venueManager, writer, reader, dataPath);
      ConsoleClient client = new ConsoleClient(addr, commandManager);
      client.run();
    } catch (NumberFormatException e) {
      System.out.println("Ошибка: порт должен быть целым числом.");
    }
  }
}
