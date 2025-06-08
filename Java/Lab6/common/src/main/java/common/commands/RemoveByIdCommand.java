package common.commands;

import common.exceptions.CommandArgumentException;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.system.utils.TextColor;
import java.util.Scanner;

/**
 * Класс команды удаляющий билет с определённым ID
 *
 * @author Maks
 * @version 1.0
 */
public class RemoveByIdCommand implements Command {
  private final CollectionManager collectionManager;

  public RemoveByIdCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    String[] args = request.getRequestBody().getArgs();

    try {
      int id = Integer.parseInt(args[0]);
      boolean removed = collectionManager.removeById(id);

      if (removed) {
        return new Response(TextColor.formatSuccess("Билет с id " + id + " удалён"));
      } else {
        return new Response(TextColor.formatError("Билет с id " + id + " не найден"));
      }
    } catch (NumberFormatException e) {
      return new Response(TextColor.formatError("Ошибка: id должен быть целым числом!"));
    }
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) throws CommandArgumentException {
    if (args.length != 1) {
      throw new CommandArgumentException("remove_by_id id", args.length);
    }

    int id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      throw new CommandArgumentException("ID должен быть числом", e);
    }

    return new RequestBody(args);
  }

  @Override
  public String getDescription() {
    return "удаляет билет по его id";
  }
}
