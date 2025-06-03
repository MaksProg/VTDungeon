package common.commands;

import common.data.Ticket;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Deque;
import java.util.Scanner;

/**
 * Класс команды которая выводит информацию по коллекции
 *
 * @author Maks
 * @version 1.0
 */
public class InfoCommand implements Command {
  private final CollectionManager collectionManager;

  public InfoCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    return new RequestBody(args);
  }

  @Override
  public Response execute(Request request) {
    Deque<Ticket> tickets = collectionManager.getDequeCollection();
    StringBuilder sb = new StringBuilder("Информация о коллекции:\n");
    sb.append("Тип коллекции:")
        .append(tickets.getClass().getSimpleName())
        .append("\nДата инициализации:")
        .append(collectionManager.getInitDate())
        .append("\nКоличество элементов:")
        .append(tickets.size());

    return new Response(sb.toString().trim());
  }

  @Override
  public String getDescription() {
    return "выводит информацию по коллекции";
  }
}
