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

public class RemoveAnyByVenueCommand extends Command {
  private final CollectionManager collectionManager;

  public RemoveAnyByVenueCommand(CollectionManager collectionManager) {
    super("remove_any_by_venue");
    this.collectionManager = collectionManager;
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Venue venue = VenueGenerator.createVenue(in);
    return new RequestBodyWithVenue(args, venue);
  }

  @Override
  public Response execute(Request request) {
    if (request.getAuth() == null) {
      return new Response(TextColor.formatError("Ошибка: пользователь не авторизован."));
    }

    RequestBody requestBody = request.getRequestBody();
    if (!(requestBody instanceof RequestBodyWithVenue bodyWithVenue)) {
      return new Response(TextColor.formatError("Ошибка: ожидался объект Venue."));
    }

    Venue inputVenue = bodyWithVenue.getVenue();
    String currentUsername = request.getAuth().username();

    Iterator<Ticket> iterator = collectionManager.getDequeCollection().iterator();
    while (iterator.hasNext()) {
      Ticket ticket = iterator.next();
      Venue venue = ticket.getVenue();

      if (venue != null
          && venue.equals(inputVenue)
          && ticket.getOwnerUsername().equals(currentUsername)) {
        boolean deleted = collectionManager.removeById(ticket.getId(), currentUsername);
        if (deleted) {
          iterator.remove();
          return new Response("Билет с указанным venue удалён.");
        } else {
          return new Response(TextColor.formatError("Не удалось удалить билет из базы данных."));
        }
      }
    }

    return new Response(
        TextColor.formatError("Билет с таким venue, принадлежащий вам, не найден."));
  }

  @Override
  public String getDescription() {
    return "удаляет один билет с эквивалентным Venue, принадлежащий текущему пользователю";
  }
}
