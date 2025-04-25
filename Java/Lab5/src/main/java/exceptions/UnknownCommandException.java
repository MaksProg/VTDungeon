package exceptions;

import system.TextColor;

/**
 * Исключение, которое обрабатывает ситуацию когда пользователь вводит неизвестную команду
 *
 * @author Maks
 * @version 1.0
 */
public class UnknownCommandException extends Exception {
  public UnknownCommandException(String command) {
    super(TextColor.ANSI_RED + "Неизвестная команда:" + command + TextColor.ANSI_RESET);
  }
}
