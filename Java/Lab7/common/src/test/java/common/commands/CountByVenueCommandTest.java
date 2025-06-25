package common.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.data.*;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithVenue;
import common.network.Response;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountByVenueCommandTest {

  private CollectionManager collectionManager;
  private CountByVenueCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);
    command = new CountByVenueCommand(collectionManager);
  }

  @Test
  void execute_notAuthorized_returnsError() {
    Request request = new Request("count_by_venue", new RequestBody(new String[] {}), null);

    Response response = command.execute(request);

    assertEquals(
        "Ошибка: необходимо авторизоваться перед выполнением этой команды.", response.getMessage());
  }

  @Test
  void execute_invalidRequestBody_returnsError() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    RequestBody wrongBody = new RequestBody(new String[] {});
    Request request = new Request("count_by_venue", wrongBody, auth);

    Response response = command.execute(request);

    assertEquals("Ошибка: ожидался объект Venue.", response.getMessage());
  }

  @Test
  void execute_validRequest_returnsCount() {
    AuthCredentials auth = new AuthCredentials("user", "pass");

    Venue venue = new Venue();
    venue.setName("Venue1");
    venue.setCapacity(100);
    venue.setType(VenueType.BAR);
    venue.setAddress(new Address("12345", new Location(10L, 20L, "TestTown")));

    Ticket ticket1 = new Ticket();
    ticket1.setVenue(venue);

    Ticket ticket2 = new Ticket();
    ticket2.setVenue(venue);

    Ticket ticket3 = new Ticket();
    ticket3.setVenue(null);

    Deque<Ticket> tickets = new ArrayDeque<>();
    tickets.add(ticket1);
    tickets.add(ticket2);
    tickets.add(ticket3);

    when(collectionManager.getDequeCollection()).thenReturn(tickets);

    RequestBodyWithVenue bodyWithVenue = new RequestBodyWithVenue(new String[] {}, venue);
    Request request = new Request("count_by_venue", bodyWithVenue, auth);

    Response response = command.execute(request);

    assertEquals("Количество билетов с введённой площадкой: 2", response.getMessage());
  }

  @Test
  void packageBody_createsRequestBodyWithVenue() {
    String inputData =
        String.join(
            System.lineSeparator(), "TestVenue", "1000", "bar", "12345", "TestTown", "10", "20");

    Scanner scanner = new Scanner(new java.io.ByteArrayInputStream(inputData.getBytes()));

    RequestBody body = command.packageBody(new String[] {"arg"}, scanner);

    assertNotNull(body);
    assertInstanceOf(RequestBodyWithVenue.class, body);

    Venue venue = ((RequestBodyWithVenue) body).getVenue();
    assertEquals("TestVenue", venue.getName());
    assertEquals(1000, venue.getCapacity());
    assertEquals(common.data.VenueType.BAR, venue.getType());
    assertEquals("12345", venue.getAddress().getZipCode());
    assertEquals("TestTown", venue.getAddress().getTown().getName());
    assertEquals(10L, venue.getAddress().getTown().getX());
    assertEquals(20L, venue.getAddress().getTown().getY());
  }

  @Test
  void getDescription_returnsExpectedString() {
    assertEquals("считает количество билетов с одинаковым Venue", command.getDescription());
  }
}
