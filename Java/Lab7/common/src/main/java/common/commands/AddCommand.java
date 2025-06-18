package common.commands;

import common.data.Ticket;
import common.data.generators.TicketGenerator;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import common.system.utils.TextColor;

import java.time.ZonedDateTime;
import java.util.Scanner;

/**
 * Команда добавления нового билета в коллекцию.
 */
public class AddCommand extends Command {
  private final CollectionManager collectionManager;

  public AddCommand(CollectionManager collectionManager) {
    super("add");
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    RequestBody body = request.getRequestBody();

    if (!(body instanceof RequestBodyWithTicket)) {
      return new Response(TextColor.formatError("Ошибка: ожидался объект Ticket"));
    }

    Ticket newTicket = ((RequestBodyWithTicket) body).getTicket();
    newTicket.setCreationDate(ZonedDateTime.now());
    newTicket.setOwnerUsername(request.getAuth().username());

    try {
      collectionManager.addTicket(newTicket); 
      return new Response("Билет успешно добавлен (ID: " + newTicket.getId() + ")");
    } catch (Exception e) {
      return new Response(TextColor.formatError("Ошибка при добавлении билета: " + e.getMessage()));
    }
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Ticket ticket = TicketGenerator.createTicket(in); // VenueManager больше не нужен
    return new RequestBodyWithTicket(args, ticket);
  }

  @Override
  public String getDescription() {
    return "добавляет билет в коллекцию";
  }
}
