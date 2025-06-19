package common.commands;

import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;

public class ClearCommand extends Command {
  private final CollectionManager collectionManager;

  public ClearCommand(CollectionManager collectionManager) {
    super("clear");
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    var user = request.getAuth();
    if (user == null) {
      return new Response("Ошибка: пользователь не авторизован.");
    }

    int deleted = collectionManager.clearCollection(request.getAuth().username());
    return new Response(
        "Удалено " + deleted + " билетов, принадлежащих пользователю " + user.username() + ".");
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    return new RequestBody(args);
  }

  @Override
  public String getDescription() {
    return "очищает коллекцию, удаляя все билеты, принадлежащие текущему пользователю";
  }
}
