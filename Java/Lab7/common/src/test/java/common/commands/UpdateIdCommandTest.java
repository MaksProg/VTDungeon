package common.commands;

import static org.junit.jupiter.api.Assertions.*;

import common.data.*;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.managers.InMemoryCollectionManager;
import common.network.Request;
import common.network.RequestBodyWithTicket;
import common.network.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateIdCommandTest {

  private CollectionManager collectionManager;
  private UpdateIdCommand command;

  @BeforeEach
  void setup() {
    collectionManager = new InMemoryCollectionManager();
    command = new UpdateIdCommand(collectionManager);

    Ticket existingTicket =
        new Ticket("OldName", new Coordinates(1.0, 2), 100.0, TicketType.USUAL, null);
    existingTicket.setId(1);
    existingTicket.setOwnerUsername("user1");

    collectionManager.getDequeCollection().add(existingTicket);
  }

  @Test
  void testSuccessfulUpdateByOwner() {
    Ticket updatedTicket =
        new Ticket("NewName", new Coordinates(10.0, 20), 500.0, TicketType.VIP, null);
    Request request =
        new Request(
            "update_by_id",
            new RequestBodyWithTicket(new String[] {"1"}, updatedTicket),
            new AuthCredentials("user1", "pass"));

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("успешно обновлён"));
    Ticket ticket =
        collectionManager.getDequeCollection().stream()
            .filter(t -> t.getId() == 1)
            .findFirst()
            .orElse(null);

    assertNotNull(ticket);
    assertEquals("NewName", ticket.getName());
    assertEquals(TicketType.VIP, ticket.getType());
  }

  @Test
  void testUpdateWithWrongOwner() {
    Ticket updatedTicket =
        new Ticket("NewName", new Coordinates(10.0, 20), 500.0, TicketType.VIP, null);
    Request request =
        new Request(
            "update_by_id",
            new RequestBodyWithTicket(new String[] {"1"}, updatedTicket),
            new AuthCredentials("other_user", "pass"));

    Response response = command.execute(request);
    assertTrue(response.getMessage().contains("не найден или вы не являетесь его владельцем"));
  }

  @Test
  void testUpdateWithInvalidId() {
    Ticket updatedTicket =
        new Ticket("Name", new Coordinates(0.0, 0), 100.0, TicketType.CHEAP, null);
    Request request =
        new Request(
            "update_by_id",
            new RequestBodyWithTicket(new String[] {"abc"}, updatedTicket),
            new AuthCredentials("user1", "pass"));

    Response response = command.execute(request);
    assertTrue(response.getMessage().contains("ID должен быть целым числом"));
  }

  @Test
  void testUpdateWithMissingTicket() {
    Request request =
        new Request(
            "update_by_id",
            new RequestBodyWithTicket(new String[] {"1"}, null),
            new AuthCredentials("user1", "pass"));

    Response response = command.execute(request);
    // Команда всё равно пойдёт на update, просто передаст null билет. Дополнительно можешь добавить
    // проверку на null
    assertTrue(response.getMessage().contains("Не передан билет для обновления."));
  }

  @Test
  void testUpdateWithoutAuth() {
    Ticket updatedTicket =
        new Ticket("Name", new Coordinates(0.0, 0), 100.0, TicketType.CHEAP, null);
    Request request =
        new Request(
            "update_by_id",
            new RequestBodyWithTicket(new String[] {"1"}, updatedTicket),
            null // без авторизации
            );

    Response response = command.execute(request);
    assertTrue(
        response.getMessage().contains("Для выполнения команды необходимо быть авторизованным."));
  }
}
