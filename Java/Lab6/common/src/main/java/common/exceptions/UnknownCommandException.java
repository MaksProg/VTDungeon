package common.exceptions;

import common.system.utils.TextColor;

/**
 * Исключение, которое обрабатывает ситуацию когда пользователь вводит неизвестную команду
 *
 * @author Maks
 * @version 1.0
 */
public class UnknownCommandException extends Exception {
  public UnknownCommandException(String command) {
    super(TextColor.formatError("Неизвестная команда: ") + command);
  }
}
