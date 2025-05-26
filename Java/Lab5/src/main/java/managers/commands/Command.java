package managers.commands;

/**
 * Интерфейс для реализации команд
 *
 * @author Maks
 * @version 1.0
 */
public interface Command {
  void execute(String[] args);

  String getDescription();
}
