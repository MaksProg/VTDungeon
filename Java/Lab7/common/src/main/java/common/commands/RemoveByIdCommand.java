package common.commands;

import common.exceptions.CommandArgumentException;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import common.system.utils.TextColor;
import java.util.Scanner;

/** Класс команды удаляющий билет с определённым ID */
public class RemoveByIdCommand extends Command {
  private final CollectionManager collectionManager;

  public RemoveByIdCommand(CollectionManager collectionManager) {
    super("remove_by_id");
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    if (request.getAuth() == null) {
      return new Response(TextColor.formatError("Ошибка: пользователь не авторизован."));
    }

    String[] args = request.getRequestBody().getArgs();
    String username = request.getAuth().username();

    try {
      int id = Integer.parseInt(args[0]);
      boolean removed = collectionManager.removeById(id, username);

      if (removed) {
        return new Response(TextColor.formatSuccess("Билет с id " + id + " удалён."));
      } else {
        return new Response(
            TextColor.formatError(
                "Не удалось удалить билет. Возможно, он не существует или принадлежит другому пользователю."));
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

    try {
      Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      throw new CommandArgumentException("ID должен быть числом", e);
    }

    return new RequestBody(args);
  }

  @Override
  public String getDescription() {
    return "удаляет билет по его id, если он принадлежит текущему пользователю";
  }
}
