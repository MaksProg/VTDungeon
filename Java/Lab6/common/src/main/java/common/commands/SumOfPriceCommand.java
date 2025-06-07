package common.commands;

import common.data.Ticket;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.Scanner;

/**
 * Класс команды которая выводит сумму значений поля price всех билетов
 *
 * @author Maks
 * @version 1.0
 */
public class SumOfPriceCommand implements Command {
  private final CollectionManager collectionManager;

  public SumOfPriceCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    double sum =
        collectionManager.getDequeCollection().stream().mapToDouble(Ticket::getPrice).sum();
    String sb = "Сумма всех значений поля price:" + sum;
    return new Response(sb);
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    return new RequestBody(args);
  }

  @Override
  public String getDescription() {
    return "выводит сумму цены всех билетов";
  }
}
