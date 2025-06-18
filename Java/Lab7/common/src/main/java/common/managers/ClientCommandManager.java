package common.managers;

import common.commands.*;


import java.util.LinkedHashMap;


public class ClientCommandManager extends CommandManager {
    private final CollectionManager collectionManager;

    public ClientCommandManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        initCommands();
    }

    @Override
    protected void initCommands() {
        HelpCommand helpCommand = new HelpCommand();
        HistoryCommand historyCommand = new HistoryCommand();
        commandList = new LinkedHashMap<>();

        // Команды без авторизации (обрабатываются клиентом напрямую)
        commandList.put("help", helpCommand);
        commandList.put("history", historyCommand);
        commandList.put("execute_script", null);
        commandList.put("login", new LoginCommand());
        commandList.put("register", new RegisterCommand());

        // Команды, упаковываемые на клиенте, исполняются на сервере
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
}
