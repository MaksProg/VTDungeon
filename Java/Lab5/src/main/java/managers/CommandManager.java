package managers;

import exceptions.UnknownCommandException;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import managers.commands.*;

/**
 * Класс отвечающий за управления командами
 *
 * @author Maks
 * @version 1.0
 */
public class CommandManager {
  public static ArrayDeque<String> lastSixCommands = new ArrayDeque<>();
  private static LinkedHashMap<String, Command> commandList;

  public CommandManager() {
    commandList = new LinkedHashMap<>();
    commandList.put("help", new HelpCommand());
    commandList.put("info", new InfoCommand());
    commandList.put("show", new ShowCommand());
    commandList.put("save", new SaveCommand());
    commandList.put("clear", new ClearCommand());
    commandList.put("add", new AddCommand());
    commandList.put("exit", new ExitCommand());
    commandList.put("remove_by_id", new RemoveByIdCommand());
    commandList.put("count_by_venue", new CountByVenueCommand());
    commandList.put("sum_of_price", new SumOfPriceCommand());
    commandList.put("remove_any_by_venue", new RemoveAnyByVenueCommand());
    commandList.put("execute_script", new ExecuteScriptCommand());
    commandList.put("history", new HistoryCommand());
    commandList.put("add_if_max", new AddIfMaxCommand());
    commandList.put("remove_greater", new RemoveGreaterCommand());
  }

  /**
   * Функция которая определяет команду и запускает её
   *
   * @author Maks
   * @param commandLine команда
   */
  public void executeCommand(String commandLine) {
    String[] parts = commandLine.trim().split("\\s+", 2); // Разделяем команду и аргументы
    String commandName = parts[0];
    String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

    Command command = commandList.get(commandName);
    try {
      if (command == null) {
        throw new UnknownCommandException(commandName);
      }

      addToHistory(commandName);
      command.execute(args);
    } catch (UnknownCommandException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println("Ошибка при выполнении команды: " + e.getMessage());
    }
  }

  /**
   * Функция добавляет команды в лист для выполнения команды history
   *
   * @author Maks
   * @param commandName команда, которая добавляется команду в историю
   */
  public static void addToHistory(String commandName) {
    if (lastSixCommands.size() >= 6) {
      lastSixCommands.pollFirst(); // Удаляем самую старую команду
    }
    lastSixCommands.addLast(commandName);
  }
}
