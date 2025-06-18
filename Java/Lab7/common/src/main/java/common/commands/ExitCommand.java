package common.commands;

import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;

/**
 * Класс команды завершающей работу программы
 *
 * @author Maks
 * @version 1.0
 */
public class ExitCommand extends Command {

  public ExitCommand() {
    super("exit");
  }

  @Override
  public Response execute(Request request) {
    return new Response("Завершение программы...", true); // true = флаг остановки
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    return new RequestBody(args);
  }

  @Override
  public String getDescription() {
    return "выход из приложения";
  }
}
