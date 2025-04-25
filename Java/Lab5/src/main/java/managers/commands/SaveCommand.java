package managers.commands;

import managers.CollectionManager;
import system.ApplicationContext;

/**
 * Класс команды которая сохраняет коллекцию в файл
 *
 * @author Maks
 * @version 1.0
 */
public class SaveCommand implements Command {
  @Override
  public void execute(String[] args) {
    CollectionManager.saveCollection(
        ApplicationContext.getDataPath(), ApplicationContext.getJsonWriter());
    System.out.println("Коллекция успешно сохранена в файл: " + ApplicationContext.getDataPath());
  }
}
