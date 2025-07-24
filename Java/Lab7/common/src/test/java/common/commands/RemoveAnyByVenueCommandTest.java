package common.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import common.data.Ticket;
import common.data.Venue;
import common.data.auth.AuthCredentials;
import common.managers.CollectionManager;
import common.network.Request;
import common.network.RequestBodyWithVenue;
import common.network.Response;
import java.util.ArrayDeque;
import java.util.Deque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RemoveAnyByVenueCommandTest {

  private CollectionManager collectionManager;
  private RemoveAnyByVenueCommand command;

  @BeforeEach
  void setUp() {
    collectionManager = mock(CollectionManager.class);
    command = new RemoveAnyByVenueCommand(collectionManager);
  }

  @Test
  void execute_userNotAuthorized_returnsError() {
    Request request = new Request("remove_any_by_venue", null, null);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("Ошибка: пользователь не авторизован."));
  }

  @Test
  void execute_wrongRequestBody_returnsError() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Request request = new Request("remove_any_by_venue", null, auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("Ошибка: ожидался объект Venue."));
  }

  @Test
  void execute_noMatchingTicket_returnsNotFound() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Venue venueToRemove = new Venue("Venue1", 100, null, null);

    Ticket ticket1 = new Ticket();
    ticket1.setVenue(new Venue("OtherVenue", 50, null, null));
    ticket1.setOwnerUsername("user");
    ticket1.setId(1);

    Ticket ticket2 = new Ticket();
    ticket2.setVenue(venueToRemove);
    ticket2.setOwnerUsername("otherUser");
    ticket2.setId(2);

    Deque<Ticket> tickets = new ArrayDeque<>();
    tickets.add(ticket1);
    tickets.add(ticket2);

    when(collectionManager.getDequeCollection()).thenReturn(tickets);

    RequestBodyWithVenue body = new RequestBodyWithVenue(new String[] {}, venueToRemove);
    Request request = new Request("remove_any_by_venue", body, auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("не найден"));
  }

  @Test
  void execute_matchingTicketDeleted_returnsSuccess() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Venue venueToRemove = new Venue("Venue1", 100, null, null);

    Ticket ticket1 = new Ticket();
    ticket1.setVenue(venueToRemove);
    ticket1.setOwnerUsername("user");
    ticket1.setId(42);

    Ticket ticket2 = new Ticket();
    ticket2.setVenue(new Venue("OtherVenue", 50, null, null));
    ticket2.setOwnerUsername("user");
    ticket2.setId(43);

    Deque<Ticket> tickets = new ArrayDeque<>();
    tickets.add(ticket1);
    tickets.add(ticket2);

    when(collectionManager.getDequeCollection()).thenReturn(tickets);
    when(collectionManager.removeById(42, "user")).thenReturn(true);

    RequestBodyWithVenue body = new RequestBodyWithVenue(new String[] {}, venueToRemove);
    Request request = new Request("remove_any_by_venue", body, auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("удалён"));
    assertFalse(tickets.contains(ticket1));
  }

  @Test
  void execute_matchingTicketRemoveFails_returnsDbError() {
    AuthCredentials auth = new AuthCredentials("user", "pass");
    Venue venueToRemove = new Venue("Venue1", 100, null, null);

    Ticket ticket = new Ticket();
    ticket.setVenue(venueToRemove);
    ticket.setOwnerUsername("user");
    ticket.setId(99);

    Deque<Ticket> tickets = new ArrayDeque<>();
    tickets.add(ticket);

    when(collectionManager.getDequeCollection()).thenReturn(tickets);
    when(collectionManager.removeById(99, "user")).thenReturn(false);

    RequestBodyWithVenue body = new RequestBodyWithVenue(new String[] {}, venueToRemove);
    Request request = new Request("remove_any_by_venue", body, auth);

    Response response = command.execute(request);

    assertTrue(response.getMessage().contains("Не удалось удалить билет из базы данных."));
  }
}
