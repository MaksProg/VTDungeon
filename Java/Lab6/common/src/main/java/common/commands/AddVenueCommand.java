package common.commands;

import common.data.Venue;
import common.data.generators.VenueGenerator;
import common.managers.VenueManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithVenue;
import common.network.Response;
import java.util.Scanner;

public class AddVenueCommand implements Command {
  private final VenueManager venueManager;

  public AddVenueCommand(VenueManager venueManager) {
    this.venueManager = venueManager;
  }

  @Override
  public Response execute(Request request) {

    RequestBody requestBody = request.getRequestBody();
    if (!(requestBody instanceof RequestBodyWithVenue)) {
      return new Response("Ошибка: ожидался объект Venue");
    }

    Venue inputVenue = ((RequestBodyWithVenue) requestBody).getVenue();
    if (inputVenue != null) {
      venueManager.addVenue(inputVenue);
      return new Response("Площадка добавлена с ID: " + inputVenue.getId());
    } else {
      return new Response("Площадка не указана.");
    }
  }

  @Override
  public RequestBody packageBody(String[] args, Scanner in) {
    Venue venue = VenueGenerator.createVenue(in);
    return new RequestBodyWithVenue(args, venue);
  }

  @Override
  public String getDescription() {
    return "добавляет новую площадку в список";
  }
}
