package server;

import common.managers.CollectionManager;
import common.managers.CommandManager;
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
import server.collectionManager.SaveableCollectionManager;

/**
 * Класс ServerInstance реализует серверную часть приложения, обслуживающую подключение клиентов
 * через неблокирующий NIO-сокет.
 *
 * <p>Основные функции:
 *
 * <ul>
 *   <li>Прием новых клиентских подключений с использованием Selector и ServerSocketChannel.
 *   <li>Обработка запросов клиентов в отдельном пуле потоков {@link ExecutorService}.
 *   <li>Асинхронное чтение и запись объектов через {@link NioObjectChannelWrapper}.
 *   <li>Обработка команд с консоли сервера, таких как сохранение коллекции и корректное завершение
 *       работы сервера.
 * </ul>
 *
 * <p>Поддерживается сохранение коллекции, если {@link CollectionManager} реализует интерфейс {@link
 * SaveableCollectionManager}. Сервер логирует основные события через {@link Logger}.
 */
public class ServerInstance {
  private static final Logger logger = Logger.getLogger(ServerInstance.class.getName());
  private static final int BUFFER_SIZE = 8192;

  private final CommandManager commandManager;
  private final CollectionManager collectionManager;
  private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
  private final ExecutorService requestHandlerPool = Executors.newCachedThreadPool();

  /**
   * Создает экземпляр сервера с заданным менеджером команд и менеджером коллекции.
   *
   * @param commandManager менеджер команд для обработки клиентских запросов
   * @param collectionManager менеджер коллекции, которая может быть сохранена при необходимости
   */
  public ServerInstance(CommandManager commandManager, CollectionManager collectionManager) {
    this.commandManager = commandManager;
    this.collectionManager = collectionManager;
  }

  /**
   * Запускает сервер на указанном порту. Осуществляет цикл ожидания и обработки новых подключений и
   * входящих сообщений. Обрабатывает консольный ввод сервера для команд управления (save,
   * shutdown).
   *
   * @param port порт, на котором запускается сервер
   * @throws IOException при ошибках ввода-вывода в работе сервера
   */
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
            NioObjectChannelWrapper wrapper = (NioObjectChannelWrapper) key.attachment();

            if (!wrapper.checkForMessage()) continue;

            Object payload;
            try {
              payload = wrapper.getPayload();
            } catch (ClassNotFoundException e) {
              logger.warning("Не удалось десериализовать объект от клиента: " + e.getMessage());
              continue;
            }

            if (payload instanceof Request request) {
              logger.info("Получен запрос: " + request.getCommandName());
              requestHandlerPool.submit(
                  () -> {
                    Response response = commandManager.handleRequest(request);
                    try {
                      wrapper.sendMessage(response);
                      logger.info("Ответ отправлен клиенту");
                    } catch (IOException e) {
                      logger.warning("Не удалось отправить ответ клиенту: " + e.getMessage());
                      key.cancel();
                      try {
                        wrapper.getChannel().close();
                      } catch (IOException ex) {
                        logger.warning("Ошибка при закрытии канала клиента");
                      }
                    }
                  });
            }

            wrapper.clearInBuffer();
          }
        } catch (CancelledKeyException e) {
          logger.warning("Ключ отменён, клиент отключён.");
        }
      }
    }
  }

  /**
   * Проверяет наличие ввода с консоли сервера. Поддерживает команды:
   *
   * <ul>
   *   <li><b>shutdown</b> — сохранение коллекции и завершение работы сервера;
   *   <li><b>save</b> — ручное сохранение коллекции;
   *   <li>прочие — вывод сообщения о неизвестной команде.
   * </ul>
   *
   * @return true, если была введена команда завершения работы сервера; иначе false
   * @throws IOException при ошибках чтения из консоли
   */
  private boolean acceptConsoleInput() throws IOException {
    if (System.in.available() > 0) {
      String command = in.readLine();
      switch (command) {
        case "shutdown" -> {
          System.out.println("Сохранение коллекции перед выходом...");
          if (collectionManager instanceof SaveableCollectionManager saveable) {
            saveable.save();
          }
          System.out.println("Завершение работы сервера...");
          return true;
        }
        case "save" -> {
          if (collectionManager instanceof SaveableCollectionManager saveable) {
            saveable.save();
            System.out.println("Коллекция успешно сохранена.");
          } else {
            System.out.println("Коллекция не поддерживает ручное сохранение.");
          }
        }
        default -> System.out.println("Неизвестная команда. Доступные: save, shutdown");
      }
    }
    return false;
  }

  /**
   * Корректно завершает работу сервера, закрывая селектор, серверный сокет и пул потоков.
   *
   * @param selector селектор, управляющий каналами
   * @param serverSocket серверный сокет
   * @throws IOException при ошибках закрытия ресурсов
   */
  private void shutdown(Selector selector, ServerSocketChannel serverSocket) throws IOException {
    selector.close();
    serverSocket.close();
    requestHandlerPool.shutdown();
  }
}
