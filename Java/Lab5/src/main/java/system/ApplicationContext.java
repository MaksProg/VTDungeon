package system;

import managers.CollectionManager;
import managers.CommandManager;

/**
 * Класс отвечающий за хранение экземпляров ключевых классов
 *
 * @author Maks
 * @version 1.0
 */
public class ApplicationContext {
  private final String dataPath;
  private final JSONReader jsonReader;
  private final JSONWriter jsonWriter;
  private final CollectionManager collectionManager;
  private final CommandManager commandManager;

  public ApplicationContext(
      String dataPath,
      JSONReader jsonReader,
      JSONWriter jsonWriter,
      CollectionManager collectionManager,
      CommandManager commandManager) {
    this.dataPath = dataPath;
    this.jsonReader = jsonReader;
    this.jsonWriter = jsonWriter;
    this.collectionManager = collectionManager;
    this.commandManager = commandManager;
  }

  public String getDataPath() {
    return dataPath;
  }

  public JSONReader getJsonReader() {
    return jsonReader;
  }

  public JSONWriter getJsonWriter() {
    return jsonWriter;
  }

  public CollectionManager getCollectionManager() {
    return collectionManager;
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }
}
