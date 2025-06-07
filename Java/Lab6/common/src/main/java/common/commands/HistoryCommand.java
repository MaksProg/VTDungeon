package common.commands;

import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithHistory;
import common.network.Response;
import java.util.ArrayDeque;
import java.util.Scanner;

public class HistoryCommand implements Command {
  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    return new RequestBody(args);
  }

  @Override
  public Response execute(Request request) {
    RequestBody body = request.getRequestBody();
    if (body instanceof RequestBodyWithHistory historyBody) {
      ArrayDeque<String> history = historyBody.getHistory();
      if (history == null || history.isEmpty()) {
        return new Response("История команд пуста.");
      }

      StringBuilder sb = new StringBuilder("Последние 6 команд:\n");
      for (String cmd : history) {
        sb.append(cmd).append("\n");
      }
      return new Response(sb.toString().trim());
    } else {
      return new Response("Ошибка: не передана история команд.");
    }
  }

  @Override
  public String getDescription() {
    return "выводит последние 6 команд без аргументов";
  }
}
