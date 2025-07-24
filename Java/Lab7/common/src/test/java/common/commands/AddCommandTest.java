package common.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.data.Ticket;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddCommandTest {

  private CollectionManager collectionManager;
  private AddCommand addCommand;

  @BeforeEach
  void setup() {
    collectionManager = mock(CollectionManager.class);
    addCommand = new AddCommand(collectionManager);
  }

  @Test
  void execute_withValidRequest_addsTicketAndReturnsSuccess() throws SQLException {
    Ticket ticket = new Ticket();
    ticket.setName("Test Ticket");

    RequestBodyWithTicket body = new RequestBodyWithTicket(new String[] {}, ticket);

    AuthCredentials auth = new AuthCredentials("testUser", "password");

    Request request = new Request("add", body, auth);

    doNothing().when(collectionManager).addTicket(ticket);

    Response response = addCommand.execute(request);

    assertNotNull(ticket.getCreationDate());
    assertEquals("testUser", ticket.getOwnerUsername());

    verify(collectionManager).addTicket(ticket);

    assertTrue(response.getMessage().contains("успешно добавлен"));
  }

  @Test
  void execute_withInvalidRequestBody_returnsError() {
    RequestBody invalidBody = new RequestBody(new String[] {});
    AuthCredentials auth = new AuthCredentials("testUser", "password");

    Request request = new Request("add", invalidBody, auth);

    Response response = addCommand.execute(request);

    assertTrue(response.getMessage().toLowerCase().contains("ошибка"));
  }
}
