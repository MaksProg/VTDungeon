package common.commands;

import static common.managers.IdManager.ticketIdGenerator;
import static common.managers.IdManager.venueIdGenerator;

import common.data.Ticket;
import common.data.Venue;
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
 * Класс команды добавляющей и затем валидирующей новый билет
 *
 * @author Maks
 * @version 1.0
 */
public class AddCommand implements Command {
  private final CollectionManager collectionManager;
  private final VenueManager venueManager;

  public AddCommand(CollectionManager collectionManager, VenueManager venueManager) {
    this.collectionManager = collectionManager;
    this.venueManager = venueManager;
  }

  @Override
  public Response execute(Request request) {
    RequestBody body = request.getRequestBody();

    if (!(body instanceof RequestBodyWithTicket)) {
      return new Response("Ошибка: ожидался объект Ticket");
    }

    try {
      Ticket newTicket = ((RequestBodyWithTicket) body).getTicket();
      newTicket.setId(ticketIdGenerator.generateId());

      if (newTicket.getVenue() != null) {
        Venue venue = newTicket.getVenue();
        venue.setId(venueIdGenerator.generateId());
        venueManager.addVenue(venue);
      }

      collectionManager.addTicket(newTicket);
      return new Response("Билет успешно добавлен");

    } catch (Exception e) {
      return new Response(TextColor.formatError("Ошибка при добавлении билета"));
    }
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Ticket ticket = TicketGenerator.createTicket(venueManager, in);
    return new RequestBodyWithTicket(args, ticket);
  }

  @Override
  public String getDescription() {
    return "добавляет билет в коллекцию";
  }
}
