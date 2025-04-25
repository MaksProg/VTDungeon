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
  public static final String DATA_PATH = System.getenv("TICKETS_FILE");
  private static final JSONReader jsonReader = new JSONReader();
  private static final JSONWriter jsonWriter = new JSONWriter();
  private static final CollectionManager collectionManager = new CollectionManager();
  private static final CommandManager commandManager = new CommandManager();

  public static String getDataPath() {
    return DATA_PATH;
  }

  public static JSONWriter getJsonWriter() {
    return jsonWriter;
  }

  public static JSONReader getJsonReader() {
    return jsonReader;
  }

  public static CollectionManager getCollectionManager() {
    return collectionManager;
  }

  public static CommandManager getCommandManager() {
    return commandManager;
  }
}
