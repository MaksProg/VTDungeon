package common.data.generators;

import static org.junit.jupiter.api.Assertions.*;

import common.data.*;
import common.system.InputManager;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class TicketGeneratorTest {

  @Test
  void testCreateTicket_withValidInputAndNullVenue() {
    String input = String.join("\n", "Концерт", "123.45", "50", "1500", "vip", "1");
    Scanner scanner = new Scanner(input);
    InputManager.setScanner(scanner);

    Ticket ticket = TicketGenerator.createTicket(scanner);

    assertNotNull(ticket);
    assertEquals("Концерт", ticket.getName());
    assertEquals(123.45, ticket.getCoordinates().getX());
    assertEquals(50, ticket.getCoordinates().getY());
    assertEquals(1500, ticket.getPrice());
    assertEquals(TicketType.VIP, ticket.getType());
    assertNull(ticket.getVenue());
  }
}
