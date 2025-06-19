package client;

import common.managers.ClientCommandManager;
import common.managers.CollectionManager;
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

      CollectionManager emptyCollectionManager = new EmptyCollectionManager();

      ClientCommandManager commandManager = new ClientCommandManager(emptyCollectionManager);

      ConsoleClient client = new ConsoleClient(addr, commandManager);
      client.run();

    } catch (NumberFormatException e) {
      System.out.println("Ошибка: порт должен быть целым числом.");
    }
  }
}
