package server;

import common.managers.CollectionManager;
import common.managers.ServerCommandManager;
import common.network.Request;
import common.network.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Logger;
import server.userManagers.SqlUserManager;

public class ServerInstance {
  private static final Logger logger = Logger.getLogger(ServerInstance.class.getName());

  private final ServerCommandManager commandManager;
  private final CollectionManager collectionManager;
  private final SqlUserManager userManager;
  private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

  private final ExecutorService responseSenderPool = Executors.newFixedThreadPool(4);

  public ServerInstance(
      ServerCommandManager commandManager,
      CollectionManager collectionManager,
      SqlUserManager userManager) {
    this.commandManager = commandManager;
    this.collectionManager = collectionManager;
    this.userManager = userManager;
  }

  public void run(int port) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress(port));
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);

    logger.info("Сервер запущен на порту " + port);

    while (true) {
      if (acceptConsoleInput()) {
        shutdown(selector, serverSocket);
        return;
      }

      if (selector.select(100) == 0) continue;

      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> it = keys.iterator();

      while (it.hasNext()) {
        SelectionKey key = it.next();
        it.remove();

        try {
          if (key.isAcceptable()) {
            SocketChannel clientChannel = serverSocket.accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(
                selector, SelectionKey.OP_READ, new NioObjectChannelWrapper(clientChannel));
            logger.info("Новое подключение: " + clientChannel.getRemoteAddress());
          } else if (key.isReadable()) {
            handleRead(key);
          }
        } catch (CancelledKeyException e) {
          logger.warning("Ключ отменён, клиент отключён.");
        }
      }
    }
  }

  private void handleRead(SelectionKey key) {
    NioObjectChannelWrapper wrapper = (NioObjectChannelWrapper) key.attachment();

    try {
      if (!wrapper.checkForMessage()) return;

      Object payload;
      try {
        payload = wrapper.getPayload();
      } catch (ClassNotFoundException e) {
        logger.warning("Не удалось десериализовать объект от клиента: " + e.getMessage());
        return;
      }

      if (payload instanceof Request request) {
        logger.info("Получен запрос: " + request.getCommandName());

        new Thread(
                () -> {
                  Response response = commandManager.handleRequest(request);

                  responseSenderPool.submit(
                      () -> {
                        try {
                          wrapper.sendMessage(response);
                          logger.info("Ответ отправлен клиенту");
                        } catch (IOException e) {
                          logger.warning("Не удалось отправить ответ клиенту: " + e.getMessage());
                          closeClientConnection(key, wrapper);
                        }
                      });
                })
            .start();
      }

      wrapper.clearInBuffer();

    } catch (IOException e) {
      logger.info("Клиент отключился: " + e.getMessage());
      closeClientConnection(key, wrapper);
    }
  }

  private boolean acceptConsoleInput() throws IOException {
    if (System.in.available() > 0) {
      String command = in.readLine();
      if (command.equals("shutdown")) {
        System.out.println("Завершение работы сервера...");
        return true;
      } else {
        System.out.println("Неизвестная команда. Доступные: save, shutdown");
      }
    }
    return false;
  }

  private void shutdown(Selector selector, ServerSocketChannel serverSocket) throws IOException {
    selector.close();
    serverSocket.close();
    responseSenderPool.shutdown();
  }

  private void closeClientConnection(SelectionKey key, NioObjectChannelWrapper wrapper) {
    try {
      key.cancel();
      wrapper.getChannel().close();
      logger.info("Клиентское соединение закрыто.");
    } catch (IOException ex) {
      logger.warning("Ошибка при закрытии канала клиента: " + ex.getMessage());
    }
  }
}
