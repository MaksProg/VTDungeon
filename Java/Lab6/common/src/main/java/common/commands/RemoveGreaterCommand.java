package common.commands;

import common.data.Ticket;
import common.data.generators.TicketGenerator;
import common.managers.CollectionManager;
import common.managers.VenueManager;
import common.network.*;
import common.system.utils.TextColor;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Класс, реализующий команду, которая удаляет все билеты большие чем ввёл пользователь
 *
 * @author Maks
 * @version 1.0
 */
public class RemoveGreaterCommand implements Command {
  private final CollectionManager collectionManager;
  private final VenueManager venueManager;

  public RemoveGreaterCommand(CollectionManager collectionManager, VenueManager venueManager) {
    this.collectionManager = collectionManager;
    this.venueManager = venueManager;
  }

  @Override
  public Response execute(Request request) {
    RequestBody body = request.getRequestBody();

    if (!(body instanceof RequestBodyWithTicket)) {
      return new Response("Ошибка: ожидался объект Ticket");
    }

    Ticket compareTicket = ((RequestBodyWithTicket) body).getTicket();

    try {
      Iterator<Ticket> iterator = collectionManager.getDequeCollection().iterator();

      int removedCount = 0;
      while (iterator.hasNext()) {
        Ticket ticket = iterator.next();
        if (ticket.compareTo(compareTicket) > 0) {
          iterator.remove();
          removedCount++;
        }
      }
      return new Response("Удалено билетов: " + removedCount);
    } catch (NumberFormatException e) {
      return new Response(TextColor.formatError("Неверный формат числа"));
    }
  }

  public RequestBody packageBody(String[] args, Scanner in) {
    Ticket ticket = TicketGenerator.createTicket(venueManager, in);
    return new RequestBodyWithTicket(args, ticket);
  }

  @Override
  public String getDescription() {
    return "удаляет все превосходящие по значению билеты";
  }
}
