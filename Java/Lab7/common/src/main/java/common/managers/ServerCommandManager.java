package common.managers;

import common.commands.*;
import common.data.auth.AuthCredentials;
import common.managers.auth.UserManager;
import common.network.Request;
import common.network.Response;
import java.util.LinkedHashMap;

public class ServerCommandManager extends CommandManager {
  private final CollectionManager collectionManager;
  private final UserManager userManager;

  public ServerCommandManager(CollectionManager collectionManager, UserManager userManager) {
    this.collectionManager = collectionManager;
    this.userManager = userManager;
    initCommands();
  }

  @Override
  protected void initCommands() {
    HelpCommand helpCommand = new HelpCommand();
    HistoryCommand historyCommand = new HistoryCommand();
    commandList = new LinkedHashMap<>();

    // Команды без авторизации
    commandList.put("help", helpCommand);
    commandList.put("history", historyCommand);
    commandList.put("register", new RegisterCommand(userManager));
    commandList.put("login", new LoginCommand(userManager));

    // Команды, требующие авторизации
    commandList.put("info", new InfoCommand(collectionManager));
    commandList.put("show", new ShowCommand(collectionManager));
    commandList.put("clear", new ClearCommand(collectionManager));
    commandList.put("add", new AddCommand(collectionManager));
    commandList.put("remove_by_id", new RemoveByIdCommand(collectionManager));
    commandList.put("count_by_venue", new CountByVenueCommand(collectionManager));
    commandList.put("sum_of_price", new SumOfPriceCommand(collectionManager));
    commandList.put("remove_any_by_venue", new RemoveAnyByVenueCommand(collectionManager));
    commandList.put("add_if_max", new AddIfMaxCommand(collectionManager));
    commandList.put("remove_greater", new RemoveGreaterCommand(collectionManager));
    commandList.put("update", new UpdateIdCommand(collectionManager));
    commandList.put("add_venue", new AddVenueCommand());
    commandList.put("exit", new ExitCommand());

    helpCommand.setCommandManager(this);
  }

  public Response handleRequest(Request request) {
    Command command = commandList.get(request.getCommandName());

    if (command == null) {
      return new Response("Команда не найдена: " + request.getCommandName());
    }

    if (command.requiresAuth()) {
      AuthCredentials creds = request.getAuth();
      if (creds == null) {
        return new Response("Ошибка: требуется авторизация.");
      }

      Integer authResult = userManager.authenticate(creds);
      if (authResult == null) {
        return new Response("Ошибка: неверный логин или пароль.");
      }
    }

    addToHistory(request.getCommandName());
    return command.execute(request);
  }
}
