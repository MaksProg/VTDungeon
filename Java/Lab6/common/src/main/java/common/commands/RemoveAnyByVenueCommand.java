package common.commands;

import common.data.Ticket;
import common.data.Venue;
import common.data.generators.VenueGenerator;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithVenue;
import common.network.Response;
import common.system.utils.TextColor;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Класс команды которая удаляет один элемент с определённым venue
 *
 * @author Maks
 * @version 1.0
 */
public class RemoveAnyByVenueCommand implements Command {
  private final CollectionManager collectionManager;

  public RemoveAnyByVenueCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Venue venue = VenueGenerator.createVenue(in);
    return new RequestBodyWithVenue(args, venue);
  }

  @Override
  public Response execute(Request request) {

    RequestBody requestBody = request.getRequestBody();
    if (!(requestBody instanceof RequestBodyWithVenue)) {
      return new Response("Ошибка: ожидался объект Venue");
    }

    Venue inputVenue = ((RequestBodyWithVenue) requestBody).getVenue();
    Iterator<Ticket> iterator = collectionManager.getDequeCollection().iterator();
    while (iterator.hasNext()) {
      Ticket ticket = iterator.next();
      Venue venue = ticket.getVenue();

      if (venue != null && venue.equals(inputVenue)) {
        iterator.remove();
        return new Response("Билет с указанным venue удалён.");
      }
    }
    return new Response(TextColor.formatError("Билет с таким venue не найден."));
  }

  @Override
  public String getDescription() {
    return "удаляет один билет с эквивалентным Venue";
  }
}
