package common.managers;

import common.commands.*;
import common.exceptions.CommandArgumentException;
import common.exceptions.UnknownCommandException;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.system.JSONReader;
import common.system.JSONWriter;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Класс отвечающий за управления командами
 *
 * @author Maks
 * @version 1.0
 */
public class CommandManager {
  public ArrayDeque<String> lastSixCommands = new ArrayDeque<>();
  private static LinkedHashMap<String, Command> commandList;
  private final CollectionManager collectionManager;
  private final VenueManager venueManager;
  private final JSONWriter jsonWriter;
  private final String dataPath;
  private final JSONReader jsonReader;

  public CommandManager(
      CollectionManager collectionManager,
      VenueManager venueManager,
      JSONWriter jsonWriter,
      JSONReader jsonReader,
      String dataPath) {
    this.collectionManager = collectionManager;
    this.venueManager = venueManager;
    this.jsonWriter = jsonWriter;
    this.jsonReader = jsonReader;
    this.dataPath = dataPath;

    initCommands();
  }

  private void initCommands() {
    HelpCommand helpCommand = new HelpCommand();
    ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
    HistoryCommand historyCommand = new HistoryCommand();
    commandList = new LinkedHashMap<>();
    commandList.put("help", helpCommand);
    commandList.put("info", new InfoCommand(collectionManager));
    commandList.put("show", new ShowCommand(collectionManager));
    commandList.put("clear", new ClearCommand(collectionManager));
    commandList.put("add", new AddCommand(collectionManager, venueManager));
    commandList.put("remove_by_id", new RemoveByIdCommand(collectionManager));
    commandList.put("count_by_venue", new CountByVenueCommand(collectionManager));
    commandList.put("sum_of_price", new SumOfPriceCommand(collectionManager));
    commandList.put("remove_any_by_venue", new RemoveAnyByVenueCommand(collectionManager));
    commandList.put("execute_script", executeScriptCommand);
    commandList.put("history", new HistoryCommand());
    commandList.put("add_if_max", new AddIfMaxCommand(collectionManager, venueManager));
    commandList.put("remove_greater", new RemoveGreaterCommand(collectionManager, venueManager));
    commandList.put("update", new UpdateIdCommand(collectionManager));
    commandList.put("add_venue", new AddVenueCommand(venueManager));
    commandList.put("exit", new ExitCommand());
    helpCommand.setCommandManager(this);
    executeScriptCommand.setCommandManager(this);
  }

  /**
   * Функция, которая определяет команду и запускает её
   *
   * @author Maks
   * @param commandLine команда
   */
  public Request handleCommandInput(String commandLine, Scanner in)
      throws UnknownCommandException, CommandArgumentException {
    String[] parts = commandLine.trim().split("\\s+", 2);
    String name = parts[0];
    String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

    Command command = commandList.get(name);
    if (command == null) {
      throw new UnknownCommandException(name);
    }

    addToHistory(name);

    RequestBody body = command.packageBody(args, in);
    return new Request(name, body);
  }

  public Response handleRequest(Request request) {
    Command command = commandList.get(request.getCommandName());

    if (request.getCommandName().equals("save")) {
      return new Response("Команда 'save' может использоваться только на сервере.");
    }

    if (command == null) {
      return new Response("Команда не найдена: " + request.getCommandName());
    }

    return command.execute(request);
  }

  /**
   * Функция добавляет команды в лист для выполнения команды history
   *
   * @author Maks
   * @param commandName команда, которая добавляется команду в историю
   */
  public void addToHistory(String commandName) {
    if (lastSixCommands.size() >= 6) {
      lastSixCommands.pollFirst();
    }
    lastSixCommands.addLast(commandName);
  }

  public LinkedHashMap<String, Command> getCommandList() {
    return commandList;
  }

  public ArrayDeque<String> getHistory() {
    return lastSixCommands;
  }
}
