package system;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс который запускает программу
 *
 * @author Maks
 * @version 1.0
 */
public class Console {
  private final ApplicationContext applicationContext;
  private boolean isRunning = true;

  public Console(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setRunning(boolean running) {
    isRunning = running;
  }

  public void start(InputStream input) {
    Scanner scanner = new Scanner(input);

    String path = applicationContext.getDataPath();
    if (path == null || path.isEmpty()) {
      TextColor.errorMessage("Ошибка: переменная окружения TICKETS_FILE не установлена!");
      setRunning(false);
      return;
    }

    try {
      System.out.println("Загрузка данных из файла...");
      applicationContext
          .getCollectionManager()
          .setDequeCollection(applicationContext.getJsonReader().readTicketsFromJson(path));
      TextColor.successMessage("Файл успешно загружен.");
    } catch (Exception e) {
      System.out.println("Ошибка при чтении файла: " + path);
      System.out.println(e.getMessage());
    }

    System.out.println("Добро пожаловать!");

    while (isRunning) {
      System.out.print("> ");
      System.out.flush();
      try {
        String command = scanner.nextLine().trim();
        if (!command.isEmpty()) {
          applicationContext.getCommandManager().executeCommand(command);
        }
      } catch (NoSuchElementException e) {
        System.out.println("\n[Ввод завершён пользователем (CTRL+D). Выход из программы.]");
        break;
      }
    }
  }
}
