package system;

import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class InputManager {
  private static Scanner scanner = new Scanner(System.in);

  /**
   * Универсальный метод для запроса и валидации пользовательского ввода.
   *
   * @param prompt Текст, отображаемый пользователю
   * @param parser Функция для преобразования строки в нужный тип
   * @param validator Проверка валидности значения
   * @param errorMsg Сообщение об ошибке при невалидном вводе
   * @param <T> Тип возвращаемого значения
   * @return Валидное значение типа T
   */
  public static <T> T promptValid(
      String prompt, Function<String, T> parser, Predicate<T> validator, String errorMsg) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine().trim();
      try {
        T value = parser.apply(input);
        if (validator.test(value)) {
          return value;
        } else {
          System.out.println("Ошибка: " + errorMsg);
        }
      } catch (Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
      }
    }
  }

  public static void setScanner(Scanner newScanner) {
    scanner = newScanner;
  }

  public static Scanner getScanner() {
    return scanner;
  }

  public static String nextLine() {
    return scanner.nextLine();
  }

  public static boolean hasNextLine() {
    return scanner.hasNextLine();
  }

  public static void setInput(InputStream inputStream) {
    scanner = new Scanner(inputStream);
  }
}
