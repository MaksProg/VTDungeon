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
 * Команда удаляет все билеты, превышающие заданный, принадлежащие текущему пользователю
 */
public class RemoveGreaterCommand extends Command {
  private final CollectionManager collectionManager;

  public RemoveGreaterCommand(CollectionManager collectionManager) {
    super("remove_greater");
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    if (request.getAuth()==null) {
      return new Response(TextColor.formatError("Ошибка: пользователь не авторизован."));
    }

    RequestBody body = request.getRequestBody();
    if (!(body instanceof RequestBodyWithTicket requestWithTicket)) {
      return new Response("Ошибка: ожидался объект Ticket.");
    }

    Ticket compareTicket = requestWithTicket.getTicket();
    String username = request.getAuth().username();

    Iterator<Ticket> iterator = collectionManager.getDequeCollection().iterator();
    int removedCount = 0;

    while (iterator.hasNext()) {
      Ticket ticket = iterator.next();
      if (ticket.compareTo(compareTicket) > 0 && username.equals(ticket.getOwnerUsername())) {
        boolean removed = collectionManager.removeById(ticket.getId(), username);
        if (removed) {
          iterator.remove(); // В памяти тоже удалим, если успешно удалено из БД
          removedCount++;
        }
      }
    }

    return new Response("Удалено билетов: " + removedCount);
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Ticket ticket = TicketGenerator.createTicket(in);
    return new RequestBodyWithTicket(args, ticket);
  }

  @Override
  public String getDescription() {
    return "удаляет все билеты, превышающие заданный, принадлежащие пользователю";
  }
}
