package common.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import common.data.Venue;
import common.data.auth.AuthCredentials;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithVenue;
import common.network.Response;
import org.junit.jupiter.api.Test;

class AddVenueCommandTest {

  @Test
  void testExecuteUserNotAuthorized() {
    AddVenueCommand command = new AddVenueCommand();
    Request request = new Request("add_venue", null, null); // auth == null

    Response response = command.execute(request);
    assertTrue(response.getMessage().contains("не авторизован"));
  }

  @Test
  void testExecuteWrongRequestBodyType() {
    AddVenueCommand command = new AddVenueCommand();
    Request request =
        new Request(
            "add_venue",
            new RequestBody(new String[] {""}) {},
            new AuthCredentials("user", "pass"));

    Response response = command.execute(request);
    assertTrue(response.getMessage().contains("ожидался объект Venue"));
  }

  @Test
  void testExecuteVenueIsNull() {
    AddVenueCommand command = new AddVenueCommand();
    RequestBodyWithVenue body = new RequestBodyWithVenue(new String[] {"dummyArg"}, null);
    Request request = new Request("add_venue", body, new AuthCredentials("user", "pass"));

    Response response = command.execute(request);
    assertTrue(response.getMessage().contains("Площадка не указана"));
  }

  @Test
  void testExecuteSuccess() {
    AddVenueCommand command = new AddVenueCommand();
    Venue venue = new Venue();
    venue.setName("TestVenue");
    RequestBodyWithVenue body = new RequestBodyWithVenue(new String[] {"dummyArg"}, venue);
    Request request = new Request("add_venue", body, new AuthCredentials("user", "pass"));

    Response response = command.execute(request);
    assertTrue(response.getMessage().contains("Площадка добавлена"));
    assertTrue(response.getMessage().contains("TestVenue"));
  }
}
