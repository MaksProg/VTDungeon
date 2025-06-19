package common.commands;

import common.data.Venue;
import common.data.generators.VenueGenerator;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithVenue;
import common.network.Response;
import java.util.Scanner;

public class AddVenueCommand extends Command {

  public AddVenueCommand() {
    super("add_venue");
  }

  @Override
  public Response execute(Request request) {
    if (request.getAuth().username() == null) {
      return new Response("Ошибка: пользователь не авторизован.");
    }

    RequestBody requestBody = request.getRequestBody();
    if (!(requestBody instanceof RequestBodyWithVenue)) {
      return new Response("Ошибка: ожидался объект Venue.");
    }

    Venue inputVenue = ((RequestBodyWithVenue) requestBody).getVenue();
    if (inputVenue == null) {
      return new Response("Площадка не указана.");
    }

    return new Response("Площадка добавлена: " + inputVenue.getName());
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Venue venue = VenueGenerator.createVenue(in);
    return new RequestBodyWithVenue(args, venue);
  }

  @Override
  public String getDescription() {
    return "добавляет новую площадку в систему";
  }
}
