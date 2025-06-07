package common.commands;

import static common.managers.IdManager.ticketIdGenerator;

import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;

/**
 * Класс команды очищающей коллекцию
 *
 * @author Maks
 * @version 1.0
 */
public class ClearCommand implements Command {
  private final CollectionManager collectionManager;

  public ClearCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    collectionManager.clearCollection();
    ticketIdGenerator.reset();
    return new Response("Коллекция успешно очищена.");
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {

    return new RequestBody(args);
  }

  @Override
  public String getDescription() {
    return "очищает коллекцию";
  }
}
