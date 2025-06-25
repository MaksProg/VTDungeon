package common.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import common.data.*;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddIfMaxCommandTest {
  private CollectionManager collectionManager;
  private AddIfMaxCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);
    command = new AddIfMaxCommand(collectionManager);
  }

  @Test
  void execute_ticketGreaterThanMax_addsAndReturnsSuccess() throws SQLException {
    Ticket ticket = new Ticket("test", new Coordinates(1.5, 2), 100.0, TicketType.VIP, null);
    Ticket maxTicket = new Ticket("other", new Coordinates(1.5, 2), 50.0, TicketType.CHEAP, null);

    AuthCredentials auth = new AuthCredentials("user", "pass");
    Request request =
        new Request("add_if_max", new RequestBodyWithTicket(new String[] {}, ticket), auth);

    when(collectionManager.getMaxTicket()).thenReturn(maxTicket);

    Response response = command.execute(request);

    verify(collectionManager).addTicket(ticket);
    assertTrue(response.getMessage().contains("успешно добавлен"));
  }

  @Test
  void execute_ticketLessThanMax_doesNotAdd() throws SQLException {
    Ticket maxTicket = new Ticket("Z", new Coordinates(100.6, 200), 999.0, TicketType.VIP, null);
    maxTicket.setId(1);

    Ticket ticket = new Ticket("Z", new Coordinates(100.5, 200), 30.0, TicketType.VIP, null);
    ticket.setId(0);

    when(collectionManager.getMaxTicket()).thenReturn(maxTicket);

    RequestBodyWithTicket body = new RequestBodyWithTicket(new String[] {}, ticket);
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Request request = new Request("add_if_max", body, auth);

    Response response = command.execute(request);

    verify(collectionManager, never()).addTicket(any());

    assertTrue(response.getMessage().contains("не добавлен"));
  }

  @Test
  void execute_noMax_addsTicket() throws SQLException {
    Ticket ticket = new Ticket("test", new Coordinates(1.5, 2), 100.0, TicketType.VIP, null);
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Request request =
        new Request("add_if_max", new RequestBodyWithTicket(new String[] {}, ticket), auth);

    when(collectionManager.getMaxTicket()).thenReturn(null);

    Response response = command.execute(request);

    verify(collectionManager).addTicket(ticket);
    assertTrue(response.getMessage().contains("успешно добавлен"));
  }

  @Test
  void execute_invalidBody_returnsError() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Request request = new Request("add_if_max", new RequestBody(new String[] {}), auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().toLowerCase().contains("ошибка"));
  }

  @Test
  void execute_unauthorizedUser_returnsError() {
    Request request =
        new Request(
            "add_if_max",
            new RequestBodyWithTicket(new String[] {}, new Ticket()),
            new AuthCredentials(null, ""));

    Response response = command.execute(request);

    assertTrue(response.getMessage().toLowerCase().contains("не авторизован"));
  }
}
