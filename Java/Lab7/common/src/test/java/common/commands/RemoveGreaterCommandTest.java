package common.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.data.*;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RemoveGreaterCommandTest {

  private CollectionManager collectionManager;
  private RemoveGreaterCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);
    command = new RemoveGreaterCommand(collectionManager);
  }

  private Ticket createTicket(int id, String ownerUsername, double price, Venue venue) {
    Ticket ticket =
        new Ticket("name" + id, new Coordinates(1.0, 2), price, TicketType.BUDGETARY, venue);
    ticket.setId(id);
    ticket.setOwnerUsername(ownerUsername);
    return ticket;
  }

  @Test
  void execute_removesTicketsGreaterThanGiven() {
    AuthCredentials auth = new AuthCredentials("user", "pass");

    Venue venue = new Venue();
    venue.setName("TestVenue");

    Ticket baseTicket = createTicket(1, "user", 50.0, venue);

    Ticket lowerTicket = createTicket(2, "user", 30.0, venue);
    Ticket higherTicket1 = createTicket(3, "user", 60.0, venue);
    Ticket higherTicket2 = createTicket(4, "otherUser", 70.0, venue);

    Deque<Ticket> tickets = new ArrayDeque<>();
    tickets.add(lowerTicket);
    tickets.add(higherTicket1);
    tickets.add(higherTicket2);

    when(collectionManager.getDequeCollection()).thenReturn(tickets);
    when(collectionManager.removeById(3, "user")).thenReturn(true);
    when(collectionManager.removeById(4, "user")).thenReturn(false);

    RequestBodyWithTicket body = new RequestBodyWithTicket(new String[] {}, baseTicket);
    Request request = new Request("remove_greater", body, auth);

    Response response = command.execute(request);

    assertEquals("Удалено билетов: 1", response.getMessage());
    assertFalse(tickets.contains(higherTicket1), "Ticket с id=3 должен быть удалён");
    assertTrue(tickets.contains(higherTicket2), "Ticket с id=4 не должен быть удалён");
    assertTrue(tickets.contains(lowerTicket), "Ticket с id=2 не должен быть удалён");
  }

  @Test
  void execute_notAuthorized_returnsError() {
    RequestBodyWithTicket body =
        new RequestBodyWithTicket(new String[] {}, createTicket(1, "user", 10, null));
    Request request = new Request("remove_greater", body, null);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("Ошибка: пользователь не авторизован"));
  }

  @Test
  void execute_invalidRequestBody_returnsError() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    RequestBody wrongBody = new RequestBody(new String[] {});
    Request request = new Request("remove_greater", wrongBody, auth);

    Response response = command.execute(request);

    assertEquals("Ошибка: ожидался объект Ticket.", response.getMessage());
  }
}
