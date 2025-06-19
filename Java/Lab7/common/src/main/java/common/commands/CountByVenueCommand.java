package common.commands;

import common.data.Ticket;
import common.data.Venue;
import common.data.generators.VenueGenerator;
import common.managers.CollectionManager;
import common.network.*;
import java.util.Scanner;

/** Команда, считающая количество билетов с заданным Venue (для всех пользователей). */
public class CountByVenueCommand extends Command {
  private final CollectionManager collectionManager;

  public CountByVenueCommand(CollectionManager collectionManager) {
    super("count_by_venue");
    this.collectionManager = collectionManager;
  }

  @Override
  public Response execute(Request request) {
    if (request.getAuth() == null) {
      return new Response("Ошибка: необходимо авторизоваться перед выполнением этой команды.");
    }

    RequestBody body = request.getRequestBody();
    if (!(body instanceof RequestBodyWithVenue)) {
      return new Response("Ошибка: ожидался объект Venue.");
    }

    Venue inputVenue = ((RequestBodyWithVenue) body).getVenue();

    long count =
        collectionManager.getDequeCollection().stream()
            .map(Ticket::getVenue)
            .filter(venue -> venue != null && venue.equals(inputVenue))
            .count();

    return new Response("Количество билетов с введённой площадкой: " + count);
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Venue venue = VenueGenerator.createVenue(in);
    return new RequestBodyWithVenue(args, venue);
  }

  @Override
  public String getDescription() {
    return "считает количество билетов с одинаковым Venue";
  }
}
