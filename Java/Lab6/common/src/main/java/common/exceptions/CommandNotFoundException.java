package common.exceptions;

public class CommandNotFoundException extends Exception {
  public CommandNotFoundException(String name) {
    super("Нет команды: " + name + ".");
  }
}
