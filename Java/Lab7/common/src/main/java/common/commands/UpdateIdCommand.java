package common.commands;

import common.data.*;
import common.data.generators.TicketGenerator;
import common.exceptions.CommandArgumentException;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import common.system.utils.TextColor;
import java.util.Scanner;

/**
 * Класс реализующий команду обновления элемента коллекции по айди.
 *
 * @author Maks
 * @version 1.0
 */
public class UpdateIdCommand extends Command {
  private final CollectionManager collectionManager;

  public UpdateIdCommand(CollectionManager collectionManager) {
    super("update_by_id");
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    if (!(request.getRequestBody() instanceof RequestBodyWithTicket requestBody)) {
      return new Response(TextColor.formatError("Неверный тип запроса."));
    }

    if (request.getAuth() == null) {
      return new Response(
          TextColor.formatError("Для выполнения команды необходимо быть авторизованным."));
    }

    String username = request.getAuth().username();
    String[] args = requestBody.getArgs();
    Ticket newTicket = requestBody.getTicket();

    int id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return new Response(TextColor.formatError("ID должен быть целым числом."));
    }

    boolean success = collectionManager.updateTicketById(id, newTicket, username);

    if (!success) {
      return new Response(
          TextColor.formatError("Билет с таким id не найден или вы не являетесь его владельцем."));
    }

    return new Response(TextColor.formatSuccess("Билет успешно обновлён."));
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) throws CommandArgumentException {
    if (args.length != 1) {
      throw new CommandArgumentException("update id", args.length);
    }

    try {
      Integer.parseInt(args[0]);
      Ticket ticket = TicketGenerator.createTicket(in);
      return new RequestBodyWithTicket(args, ticket);
    } catch (NumberFormatException e) {
      throw new CommandArgumentException("Не получилось привести " + args[0] + " к числу", e);
    }
  }

  @Override
  public String getDescription() {
    return "обновляет билет по id, если он принадлежит вам";
  }
}
