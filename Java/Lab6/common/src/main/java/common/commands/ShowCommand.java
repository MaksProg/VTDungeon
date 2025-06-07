package common.commands;

import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.system.utils.TextColor;
import java.util.Scanner;

/**
 * Класс команды которая выводит коллекцию
 *
 * @author Maks
 * @version 1.0
 */
public class ShowCommand implements Command {

  private final CollectionManager collectionManager;

  public ShowCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    return new RequestBody(args);
  }

  @Override
  public Response execute(Request request) {

    if (collectionManager.getDequeCollection().isEmpty()) {
      return new Response(TextColor.formatError("Коллекция пуста"));
    } else {
      StringBuilder sb = new StringBuilder();
      collectionManager.getDequeCollection().forEach(ticket -> sb.append(ticket).append("\n"));
      return new Response(sb.toString().trim());
    }
  }

  @Override
  public String getDescription() {
    return "выводит коллекцию";
  }
}
