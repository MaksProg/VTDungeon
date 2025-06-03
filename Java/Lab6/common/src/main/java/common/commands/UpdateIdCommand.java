package common.commands;

import common.data.*;
import common.exceptions.CommandArgumentException;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import common.system.utils.TextColor;
import java.util.Optional;
import java.util.Scanner;

/**
 * Класс реализующий команду обновления элемента коллекции по айди.
 *
 * @author Maks
 * @version 1.0
 */
public class UpdateIdCommand implements Command {

  private final CollectionManager collectionManager;

  public UpdateIdCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    if (!(request.getRequestBody() instanceof RequestBodyWithTicket requestBody)) {
      return new Response(
          TextColor.formatError("Неверный тип запроса: ожидался RequestBodyWithTicket"));
    }

    String[] args = requestBody.getArgs();
    Ticket newTicket = requestBody.getTicket();

    int id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return new Response(TextColor.formatError("ID должен быть целым числом."));
    }

    Optional<Ticket> existing =
        collectionManager.getDequeCollection().stream().filter(t -> t.getId() == id).findFirst();

    if (existing.isEmpty()) {
      return new Response(TextColor.formatError("Билет с id " + id + " не найден."));
    }

    newTicket.setId(id);
    collectionManager.getDequeCollection().remove(existing.get());
    collectionManager.getDequeCollection().add(newTicket);

    return new Response(TextColor.formatSuccess("Билет с id " + id + " успешно обновлён."));
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) throws CommandArgumentException {
    if (args.length != 1) {
      throw new CommandArgumentException("update id", args.length);
    }

    try {
      Integer.parseInt(args[0]);

      return new RequestBody(args);
    } catch (NumberFormatException e) {
      throw new CommandArgumentException("Не получилось привести " + args[0] + " к цифре", e);
    }
  }

  @Override
  public String getDescription() {
    return "обновляет элемент по id";
  }
}
