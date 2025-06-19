package common.commands;

import common.data.Ticket;
import common.data.generators.TicketGenerator;
import common.managers.CollectionManager;
import common.network.*;
import java.sql.SQLException;
import java.util.Scanner;

/** Команда добавляет билет, если он больше максимального в базе. */
public class AddIfMaxCommand extends Command {
  private final CollectionManager collectionManager;

  public AddIfMaxCommand(CollectionManager collectionManager) {
    super("add_if_max");
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    if (request.getAuth().username() == null) {
      return new Response("Ошибка: пользователь не авторизован.");
    }

    RequestBody body = request.getRequestBody();
    if (!(body instanceof RequestBodyWithTicket)) {
      return new Response("Ошибка: ожидался объект Ticket.");
    }

    Ticket ticket = ((RequestBodyWithTicket) body).getTicket();
    ticket.setOwnerUsername(request.getAuth().username());

    try {
      Ticket maxTicket = collectionManager.getMaxTicket();
      if (maxTicket == null || ticket.compareTo(maxTicket) > 0) {
        collectionManager.addTicket(ticket);
        return new Response("Билет успешно добавлен: он больше максимального.");
      } else {
        return new Response("Билет не добавлен: он не превышает максимальный.");
      }
    } catch (SQLException e) {
      return new Response("Ошибка при работе с базой данных: " + e.getMessage());
    }
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Ticket ticket = TicketGenerator.createTicket(in);
    return new RequestBodyWithTicket(args, ticket);
  }

  @Override
  public String getDescription() {
    return "добавляет билет, если он больше максимального в коллекции (сравнение через базу данных)";
  }
}
