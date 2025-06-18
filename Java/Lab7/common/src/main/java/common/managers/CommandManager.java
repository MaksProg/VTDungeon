package common.managers;

import common.commands.Command;
import common.data.auth.AuthCredentials;
import common.exceptions.CommandArgumentException;
import common.exceptions.UnknownCommandException;
import common.network.Request;
import common.network.RequestBody;

import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class CommandManager {
  protected ConcurrentLinkedDeque<String> lastSixCommands = new ConcurrentLinkedDeque<>();
  protected static LinkedHashMap<String, Command> commandList;

  public CommandManager() {
    initCommands();
  }

  protected abstract void initCommands();

  public Request handleCommandInput(String commandLine, Scanner in, AuthCredentials authCredentials)
          throws UnknownCommandException, CommandArgumentException {
    String[] parts = commandLine.trim().split("\\s+", 2);
    String name = parts[0];
    String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

    Command command = commandList.get(name);
    if (command == null) throw new UnknownCommandException(name);

    addToHistory(name);
    RequestBody body = command.packageBody(args, in);
    return new Request(name, body, authCredentials);
  }

  public void addToHistory(String commandName) {
    if (lastSixCommands.size() >= 6) lastSixCommands.pollFirst();
    lastSixCommands.addLast(commandName);
  }

  public LinkedHashMap<String, Command> getCommandList() {
    return commandList;
  }

  public ConcurrentLinkedDeque<String> getHistory() {
    return lastSixCommands;
  }
}
