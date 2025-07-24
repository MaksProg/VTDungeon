package common.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import common.data.Ticket;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;
import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SumOfPriceCommandTest {

  private CollectionManager collectionManager;
  private SumOfPriceCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);
    Deque<Ticket> tickets = new ArrayDeque<>();
    when(collectionManager.getDequeCollection()).thenReturn(tickets);

    command = new SumOfPriceCommand(collectionManager);
  }

  @Test
  void execute_notAuthorized_returnsError() {
    Request request = new Request("sum_of_price", new RequestBody(new String[] {}), null);

    Response response = command.execute(request);

    assertEquals(
        "Ошибка: необходимо авторизоваться перед выполнением этой команды.", response.getMessage());
  }

  @Test
  void execute_authorized_returnsSum() {
    // Подготовка билетов
    Ticket ticket1 = new Ticket();
    ticket1.setPrice(100.0);
    Ticket ticket2 = new Ticket();
    ticket2.setPrice(200.5);
    Ticket ticket3 = new Ticket();
    ticket3.setPrice(0.0);

    Deque<Ticket> tickets = collectionManager.getDequeCollection();
    tickets.add(ticket1);
    tickets.add(ticket2);
    tickets.add(ticket3);

    AuthCredentials auth = new AuthCredentials("user", "pass");
    Request request = new Request("sum_of_price", new RequestBody(new String[] {}), auth);

    Response response = command.execute(request);

    assertEquals("Сумма всех значений поля price:300.5", response.getMessage());
  }
}
