  package client;

  import common.managers.ClientControl;
  import common.managers.CommandManager;
  import common.network.Request;
  import common.network.RequestBodyWithHistory;
  import common.network.Response;
  import common.network.ResponseWithAuthCredentials;
  import common.system.InputManager;
  import common.data.auth.AuthCredentials;

  import java.io.FileInputStream;
  import java.io.IOException;
  import java.net.SocketAddress;
  import java.nio.channels.SocketChannel;
  import java.nio.file.Files;
  import java.nio.file.InvalidPathException;
  import java.nio.file.Path;
  import java.nio.file.Paths;
  import java.util.*;

  /**
   * Класс ConsoleClient ...
   */
  public class ConsoleClient implements ClientControl {
    private static final int TIMEOUT_SECONDS = 10;
    private static final int MILLIS_IN_SECOND = 1000;
    private boolean isRunning = true;
    private final Set<Path> scriptsNames = new TreeSet<>();
    private final SocketAddress serverAddress;
    private final CommandManager commandManager;
    private ObjectSocketChannelWrapper channelWrapper;

    private AuthCredentials currentAuth = null;

    public ConsoleClient(SocketAddress serverAddress, CommandManager commandManager) {
      this.serverAddress = serverAddress;
      this.commandManager = commandManager;
    }

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

    private void inputLoop() {
      Scanner scanner = new Scanner(System.in);

      while (isRunning) {

        String prompt = currentAuth != null ? currentAuth.username() + "> " : "> ";
        System.out.print(prompt);

        if (!scanner.hasNextLine()) break;
        String commandLine = scanner.nextLine();
        try {
          Request request = null;
          String[] parts = commandLine.trim().split("\\s+");
          if (parts[0].equals("history")) {
            commandManager.addToHistory("history");
             request = new Request(
                    "history",
                    new RequestBodyWithHistory(new String[0], commandManager.getHistory()),
                    currentAuth
            );
          } else if (parts[0].equals("execute_script")) {
            if (parts.length < 2) {
              System.out.println("Неверные аргументы команды");
            } else if (parts.length == 2) {
              executeScript(parts[1]);
            } else {
              System.out.println("Неверное количество аргументов");
            }
          } else {
            request = commandManager.handleCommandInput(commandLine, scanner,currentAuth);
          }

          if (request != null) {
            Request requestWithAuth = new Request(
                    request.getCommandName(),
                    request.getRequestBody(),
                    currentAuth
            );

            channelWrapper.sendMessage(requestWithAuth);
            Response response = waitForResponse();

            if (response != null) {
              System.out.println(response.getMessage());

              // Если сервер прислал новые учётные данные — обновляем currentAuth
              if (response instanceof ResponseWithAuthCredentials) {
                currentAuth = ((ResponseWithAuthCredentials) response).getAuthCredentials();
              }

              if (response.shouldStopClient()) {
                stopClient();
              }
            } else {
              System.out.println("Ответ от сервера не получен.");
            }

            channelWrapper.clearInBuffer();
          }

        } catch (IOException e) {
          System.err.println("\nСоединение с сервером было потеряно: " + e.getMessage());
          stopClient();
          System.exit(0);
        } catch (Exception e) {
          System.err.println("Ошибка: " + e.getMessage());
        }
      }
    }

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

    private void executeScript(String path) {
      if (path == null || path.isBlank()) {
        System.out.println("Неверные аргументы команды");
        return;
      }

      Path pathToScript;
      try {
        pathToScript = Paths.get(path).toAbsolutePath().normalize();

        if (scriptsNames.contains(pathToScript)) {
          System.out.println(
                  "Один и тот же скрипт не может выполняться рекурсивно: " + pathToScript.getFileName());
          return;
        }

        if (!Files.exists(pathToScript) || !Files.isReadable(pathToScript)) {
          System.out.println("Файл " + path + " не найден или нет прав на чтение");
          return;
        }

        scriptsNames.add(pathToScript);

        Scanner originalScanner = InputManager.getScanner();
        Scanner fileScanner = new Scanner(new FileInputStream(pathToScript.toFile()));
        InputManager.setScanner(fileScanner);

        while (InputManager.hasNextLine()) {
          String line = InputManager.nextLine().trim();
          if (line.isEmpty() || line.startsWith("#")) continue;

          String[] parts = line.split("\\s+");

          if (parts[0].equalsIgnoreCase("execute_script")) {
            if (parts.length < 2) {
              System.out.println("Команда execute_script должна содержать путь к файлу");
            } else {
              executeScript(parts[1]);
            }
            continue;
          }

          try {
            Request request = commandManager.handleCommandInput(line, fileScanner,currentAuth);
            if (request != null) {
              Request requestWithAuth = new Request(
                      request.getCommandName(),
                      request.getRequestBody(),
                      currentAuth
              );

              channelWrapper.sendMessage(requestWithAuth);
              Response response = waitForResponse();
              if (response != null) {
                System.out.println(response.getMessage());

                if (response instanceof ResponseWithAuthCredentials) {
                  currentAuth = ((ResponseWithAuthCredentials) response).getAuthCredentials();
                }

                if (response.shouldStopClient()) {
                  stopClient();
                  break;
                }
              } else {
                System.out.println("Ответ от сервера не получен.");
              }
              channelWrapper.clearInBuffer();
            }
          } catch (Exception e) {
            System.out.println("Ошибка при выполнении команды из скрипта: " + line);
            System.out.println(e.getMessage());
          }
        }

        InputManager.setScanner(originalScanner);

        scriptsNames.remove(pathToScript);
        System.out.println("Скрипт " + pathToScript.getFileName() + " успешно выполнен");

      } catch (IOException e) {
        System.out.println("Ошибка ввода/вывода при работе со скриптом: " + e.getMessage());
      } catch (SecurityException e) {
        System.out.println("Недостаточно прав для чтения файла " + path);
      } catch (InvalidPathException e) {
        System.out.println("Проверьте путь к файлу. В нём не должно быть лишних символов");
      }
    }

    @Override
    public void stopClient() {
      isRunning = false;
    }
  }
