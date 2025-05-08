package managers.commands;

import managers.CollectionManager;
import system.JSONWriter;

/**
 * Класс команды которая сохраняет коллекцию в файл
 *
 * @author Maks
 * @version 1.0
 */
public class SaveCommand implements Command {
  private final CollectionManager collectionManager;
  private final JSONWriter jsonWriter;
  private final String dataPath;

  public SaveCommand(CollectionManager collectionManager, JSONWriter jsonWriter, String dataPath) {
    this.collectionManager = collectionManager;
    this.jsonWriter = jsonWriter;
    this.dataPath = dataPath;
  }

  @Override
  public void execute(String[] args) {
    jsonWriter.writeTicketsToJson(collectionManager.getDequeCollection(), dataPath);
    System.out.println("Коллекция успешно сохранена в файл: " + dataPath);
  }

  @Override
  public String getDescription() {
    return "сохраняет коллекцию в файл";
  }
}
