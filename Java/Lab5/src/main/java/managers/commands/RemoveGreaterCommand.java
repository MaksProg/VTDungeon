package managers.commands;

import data.Ticket;
import data.generators.TicketGenerator;
import java.util.Iterator;
import managers.CollectionManager;
import managers.VenueManager;
import system.TextColor;

/**
 * Класс, реализующий команду, которая удаляет все билеты большие чем ввёл пользователь
 *
 * @author Maks
 * @version 1.0
 */
public class RemoveGreaterCommand implements Command {
  private final CollectionManager collectionManager;
  private final VenueManager venueManager;

  public RemoveGreaterCommand(CollectionManager collectionManager, VenueManager venueManager) {
    this.collectionManager = collectionManager;
    this.venueManager = venueManager;
  }

  @Override
  public void execute(String[] args) {

    Ticket compareTicket = TicketGenerator.createTicket(venueManager);

    try {
      Iterator<Ticket> iterator = collectionManager.getDequeCollection().iterator();

      int removedCount = 0;
      while (iterator.hasNext()) {
        Ticket ticket = iterator.next();
        if (ticket.compareTo(compareTicket) > 0) {
          iterator.remove();
          removedCount++;
        }
      }
      TextColor.successMessage("Удалено билетов: ");
      System.out.print(removedCount);
    } catch (NumberFormatException e) {
      TextColor.errorMessage("Неверный формат числа");
    }
  }

  @Override
  public String getDescription() {
    return "удаляет все превосходящие по значению билеты";
  }
}
