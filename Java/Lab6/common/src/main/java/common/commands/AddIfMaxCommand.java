package common.commands;

import common.data.Ticket;
import common.data.generators.TicketGenerator;
import common.managers.CollectionManager;
import common.managers.VenueManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import common.system.utils.TextColor;
import java.util.Scanner;

/**
 * Класс команды добавляющую билет, если вместимость площадки нового билета больше чем максимальная
 * вместимость среди билетов в коллекции
 *
 * @author Maks
 * @version 1.0
 */
public class AddIfMaxCommand implements Command {
  private final CollectionManager collectionManager;
  private final VenueManager venueManager;

  public AddIfMaxCommand(CollectionManager collectionManager, VenueManager venueManager) {
    this.collectionManager = collectionManager;
    this.venueManager = venueManager;
  }

  @Override
  public Response execute(Request request) {
    RequestBody body = request.getRequestBody();

    if (!(body instanceof RequestBodyWithTicket)) {
      return new Response("Ошибка: ожидался объект Ticket");
    }

    Ticket ticket = ((RequestBodyWithTicket) body).getTicket();
    Ticket maxTicket = collectionManager.getMaxTicket();

    if (maxTicket == null || collectionManager.getDequeCollection().isEmpty()) {
      collectionManager.addTicket(ticket);
      return new Response(TextColor.formatSuccess("Билет добавлен"));
    } else if (ticket.compareTo(maxTicket) > 0) {
      collectionManager.addTicket(ticket);
      return new Response(TextColor.formatSuccess("Билет добавлен"));
    }
    return new Response(TextColor.formatError("Билет не является максимальным"));
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Ticket ticket = TicketGenerator.createTicket(venueManager, in);
    return new RequestBodyWithTicket(args, ticket);
  }

  @Override
  public String getDescription() {
    return "добавляет билет если он максимальный";
  }
}
