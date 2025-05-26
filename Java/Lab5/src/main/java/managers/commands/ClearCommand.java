package managers.commands;

import static managers.IdManager.ticketIdGenerator;

import managers.CollectionManager;
import system.TextColor;

/**
 * Класс команды очищающей коллекцию
 *
 * @author Maks
 * @version 1.0
 */
public class ClearCommand implements Command {
  private final CollectionManager collectionManager;

  public ClearCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public void execute(String[] args) {
    collectionManager.clearCollection();
    ticketIdGenerator.reset();
    TextColor.successMessage("Коллекция успешно очищена.");
  }

  @Override
  public String getDescription() {
    return "очищает коллекцию";
  }
}
