package common.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.data.Coordinates;
import common.data.Ticket;
import common.data.TicketType;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShowCommandTest {

  private CollectionManager collectionManager;
  private ShowCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);

    Deque<Ticket> tickets = new ArrayDeque<>();
    when(collectionManager.getDequeCollection()).thenReturn(tickets);

    command = new ShowCommand(collectionManager);
  }

  @Test
  void execute_notAuthorized_returnsError() {
    Request request = new Request("show", new RequestBody(new String[] {}), null);

    Response response = command.execute(request);

    assertEquals(
        "Ошибка: необходимо авторизоваться для просмотра коллекции.", response.getMessage());
  }

  @Test
  void execute_emptyCollection_returnsEmptyMessage() {
    // Установим пользователя (для авторизации)
    AuthCredentials auth = new AuthCredentials("user", "password");
    Request request = new Request("show", new RequestBody(new String[] {}), auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("Коллекция пуста"));
  }

  @Test
  void execute_nonEmptyCollection_returnsTickets() {
    Ticket ticket1 = new Ticket("Concert1", new Coordinates(1.0, 2), 100.0, TicketType.VIP, null);
    ticket1.setCreationDate(ZonedDateTime.now());
    ticket1.setOwnerUsername("user");

    Ticket ticket2 = new Ticket("Concert2", new Coordinates(3.0, 4), 200.0, TicketType.USUAL, null);
    ticket2.setCreationDate(ZonedDateTime.now());
    ticket2.setOwnerUsername("user");

    collectionManager.getDequeCollection().add(ticket1);
    collectionManager.getDequeCollection().add(ticket2);

    AuthCredentials auth = new AuthCredentials("user", "password");
    Request request = new Request("show", new RequestBody(new String[] {}), auth);

    Response response = command.execute(request);
    String result = response.getMessage();

    assertTrue(result.contains("Concert1"));
    assertTrue(result.contains("Concert2"));
  }

  @Test
  void packageBody_returnsRequestBodyWithArgs() {
    RequestBody body = command.packageBody(new String[] {"arg1", "arg2"}, null);

    assertNotNull(body);
    assertArrayEquals(new String[] {"arg1", "arg2"}, body.getArgs());
  }

  @Test
  void getDescription_returnsCorrectString() {
    assertEquals("выводит коллекцию", command.getDescription());
  }
}
