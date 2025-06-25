package common.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import common.data.Coordinates;
import common.data.Ticket;
import common.data.TicketType;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InfoCommandTest {

  private CollectionManager collectionManager;
  private InfoCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);
    command = new InfoCommand(collectionManager);
  }

  @Test
  void execute_notAuthorized_returnsError() {
    Request request = new Request("info", new RequestBody(new String[] {}), null);

    Response response = command.execute(request);

    assertEquals(
        "Ошибка: необходимо авторизоваться для просмотра истории команд.", response.getMessage());
  }

  @Test
  void execute_authorized_returnsCollectionInfo() {
    AuthCredentials auth = new AuthCredentials("user", "pass");

    Deque<Ticket> tickets = new ArrayDeque<>();
    tickets.add(createSampleTicket());

    when(collectionManager.getDequeCollection()).thenReturn(tickets);
    when(collectionManager.getInitDate())
        .thenReturn(ZonedDateTime.of(2025, 6, 22, 12, 0, 0, 0, ZoneId.systemDefault()));

    Request request = new Request("info", new RequestBody(new String[] {}), auth);

    Response response = command.execute(request);

    String expectedStart =
        "Информация о коллекции:\n"
            + "Тип коллекции:"
            + tickets.getClass().getSimpleName()
            + "\n"
            + "Дата инициализации:"
            + collectionManager.getInitDate()
            + "\n"
            + "Количество элементов:"
            + tickets.size();

    assertEquals(expectedStart, response.getMessage());
  }

  private Ticket createSampleTicket() {
    Ticket ticket = new Ticket();
    ticket.setName("Sample");
    ticket.setCoordinates(new Coordinates(1.0, 2));
    ticket.setPrice(100.0);
    ticket.setType(TicketType.VIP);
    return ticket;
  }
}
