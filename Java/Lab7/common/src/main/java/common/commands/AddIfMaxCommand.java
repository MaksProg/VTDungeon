package common.commands;

import common.data.Ticket;
import common.managers.CollectionManager;
import common.data.auth.AuthCredentials;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;

import java.sql.SQLException;


/**
 * Команда добавляет билет, если он больше максимального по сравнению.
 */
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

    Ticket maxTicket = collectionManager.getMaxTicket();
    if (maxTicket == null || collectionManager.getDequeCollection().isEmpty()) {
        try {
            collectionManager.addTicket(ticket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Response("Билет добавлен (коллекция пуста).");
    } else if (ticket.compareTo(maxTicket) > 0) {
        try {
            collectionManager.addTicket(ticket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Response("Билет добавлен (он больше максимального).");
    }

    return new Response("Билет не является максимальным. Добавление отменено.");
  }

  @Override
  public String getDescription() {
    return "добавляет билет, если он больше максимального в коллекции";
  }
}
