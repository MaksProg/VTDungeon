package system;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import managers.CollectionManager;

/**
 * Класс который запускает программу
 *
 * @author Maks
 * @version 1.0
 */
public class Console {

  public static void start(InputStream input) {
    Scanner scanner = new Scanner(input);

    String path = ApplicationContext.getDataPath();
    if (path == null || path.isEmpty()) {
      System.out.println(
          TextColor.ANSI_RED
              + "Ошибка: переменная окружения TICKETS_FILE не установлена!"
              + TextColor.ANSI_RESET);
      System.exit(1);
    }

    try {
      System.out.println("Загрузка данных из файла...");
      CollectionManager.setDequeCollection(
          ApplicationContext.getJsonReader().readTicketsFromJson(path));
      System.out.println(TextColor.ANSI_GREEN + "Файл успешно загружен." + TextColor.ANSI_RESET);
    } catch (Exception e) {
      System.out.println("Ошибка при чтении файла: " + path);
      System.out.println(e.getMessage());
      System.exit(1);
    }

    System.out.println("Добро пожаловать!");

    while (true) {
      System.out.print("> ");
      System.out.flush();
      try {
        String command = scanner.nextLine().trim();
        if (!command.isEmpty()) {
          ApplicationContext.getCommandManager().executeCommand(command);
        }
      } catch (NoSuchElementException e) {
        System.out.println("\n[Ввод завершён пользователем (CTRL+D). Выход из программы.]");
        break;
      }
    }
  }
}
