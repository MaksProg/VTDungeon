package client;

import common.data.auth.AuthCredentials;
import common.managers.CommandManager;
import common.network.Request;
import common.network.RequestBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

class DummyCommandManager extends CommandManager {
  private final List<String> history = new ArrayList<>();

  @Override
  public Request handleCommandInput(String input, Scanner scanner, AuthCredentials auth) {
    // Возвращаем заглушку для не-history команд
    return new Request(input, new RequestBody(new String[0]), auth);
  }

  @Override
  protected void initCommands() {}

  @Override
  public void addToHistory(String commandName) {
    history.add(commandName);
  }

  @Override
  public ConcurrentLinkedDeque<String> getHistory() {
    return (ConcurrentLinkedDeque<String>) history;
  }
}
