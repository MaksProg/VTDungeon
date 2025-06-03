package client;

import common.managers.ClientControl;
import common.managers.CommandManager;
import common.network.Request;
import common.network.RequestBodyWithHistory;
import common.network.Response;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Класс ConsoleClient представляет консольный клиент, который подключается к серверу, отправляет
 * команды, полученные от пользователя, и обрабатывает ответы сервера.
 *
 * <p>Клиент использует неблокирующий SocketChannel для сетевого взаимодействия. Обработка команд
 * происходит в цикле ввода с консоли до тех пор, пока клиент не будет остановлен.
 *
 * <p>Особенности:
 *
 * <ul>
 *   <li>Отправка запросов на сервер через объект {@link ObjectSocketChannelWrapper}.
 *   <li>Ожидание ответа с таймаутом 10 секунд с отображением прогресса ожидания.
 *   <li>Обработка специальной команды "history", которая отправляет историю ранее выполненных
 *       команд.
 *   <li>Реализация интерфейса {@link ClientControl} для возможности остановки клиента извне.
 * </ul>
 */
public class ConsoleClient implements ClientControl {
  private static final int TIMEOUT_SECONDS = 10;
  private static final int MILLIS_IN_SECOND = 1000;
  private boolean isRunning = true;

  private final SocketAddress serverAddress;
  private final CommandManager commandManager;
  private ObjectSocketChannelWrapper channelWrapper;

  /**
   * Создает новый консольный клиент.
   *
   * @param serverAddress адрес сервера, к которому будет происходить подключение
   * @param commandManager менеджер команд для обработки пользовательских вводов
   */
  public ConsoleClient(SocketAddress serverAddress, CommandManager commandManager) {
    this.serverAddress = serverAddress;
    this.commandManager = commandManager;
  }

  /**
   * Запускает клиента: открывает соединение с сервером, конфигурирует неблокирующий режим, и
   * начинает цикл ввода команд с консоли. При ошибках подключения выводит сообщение в консоль.
   */
  public void run() {
    try (SocketChannel socket = SocketChannel.open()) {
      socket.connect(serverAddress);
      socket.configureBlocking(false);

      this.channelWrapper = new ObjectSocketChannelWrapper(socket);

      System.out.println("Подключено к серверу: " + serverAddress);
      inputLoop();
    } catch (IOException e) {
      System.err.println("Ошибка при подключении к серверу: " + e.getMessage());
    }
  }

  /**
   * Основной цикл ввода команд с консоли. Принимает строки с командой, формирует запросы,
   * отправляет их серверу и обрабатывает ответы. Цикл продолжается до вызова {@link #stopClient()}
   * или пока пользователь не завершит ввод.
   */
  private void inputLoop() {
    Scanner scanner = new Scanner(System.in);

    while (isRunning) {
      System.out.print("> ");
      if (!scanner.hasNextLine()) break;
      String commandLine = scanner.nextLine();

      try {
        Request request;
        if (commandLine.trim().equals("history")) {
          commandManager.addToHistory("history");
          request =
              new Request(
                  "history",
                  new RequestBodyWithHistory(new String[0], commandManager.getHistory()));
        } else {
          request = commandManager.handleCommandInput(commandLine, scanner);
        }

        if (request != null) {
          channelWrapper.sendMessage(request);
          Response response = waitForResponse();

          if (response != null) {
            System.out.println(response.getMessage());
            if (response.shouldStopClient()) {
              stopClient();
            }
          } else {
            System.out.println("Ответ от сервера не получен.");
          }

          channelWrapper.clearInBuffer();
        }

      } catch (IOException e) {
        System.err.println("Ошибка при отправке запроса: " + e.getMessage());
        return;
      } catch (Exception e) {
        System.err.println("Ошибка: " + e.getMessage());
      }
    }
  }

  /**
   * Ожидает ответ от сервера с таймаутом. В течение таймаута выводит точки прогресса ожидания.
   *
   * @return объект Response, полученный от сервера, или null, если ответ не был получен вовремя
   * @throws IOException при ошибках ввода-вывода в процессе ожидания
   */
  private Response waitForResponse() throws IOException {
    long startTime = System.currentTimeMillis();
    int secondsPassed = 0;

    while (secondsPassed < TIMEOUT_SECONDS) {
      if (channelWrapper.checkForMessage()) {
        Object payload = channelWrapper.getPayload();
        if (payload instanceof Response) {
          return (Response) payload;
        } else {
          System.out.println("Получен некорректный ответ от сервера.");
          return null;
        }
      }

      if (System.currentTimeMillis() >= startTime + (secondsPassed + 1) * MILLIS_IN_SECOND) {
        System.out.print(".");
        secondsPassed++;
      }
    }

    System.out.println("\nВремя ожидания ответа от сервера истекло.");
    return null;
  }

  /** Останавливает работу клиента, прерывая основной цикл ввода команд. */
  @Override
  public void stopClient() {
    isRunning = false;
  }
}
