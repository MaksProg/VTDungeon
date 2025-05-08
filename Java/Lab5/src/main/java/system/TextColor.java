package system;

/**
 * Класс отвечающий за цвет текста
 *
 * @author Maks
 * @version 1.0
 */
public class TextColor {
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_GREEN = "\u001B[32m";

  public static void successMessage(String message) {
    System.out.println(ANSI_GREEN + message + ANSI_RESET);
  }

  public static void errorMessage(String message) {
    System.out.println(ANSI_RED + message + ANSI_RESET);
  }

  public static String formatSuccess(String message) {
    return ANSI_GREEN + message + ANSI_RESET;
  }

  public static String formatError(String message) {
    return ANSI_RED + message + ANSI_RESET;
  }
}
