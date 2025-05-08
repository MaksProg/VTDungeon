package managers.commands;

import data.*;
import data.generators.TicketGenerator;
import managers.CollectionManager;
import managers.VenueManager;
import system.TextColor;

/**
 * Класс команды добавляющей и затем валидирующей новый билет
 *
 * @author Maks
 * @version 1.0
 */
public class AddCommand implements Command {
  private final CollectionManager collectionManager;
  private final VenueManager venueManager;

  public AddCommand(CollectionManager collectionManager, VenueManager venueManager) {
    this.collectionManager = collectionManager;
    this.venueManager = venueManager;
  }

  @Override
  public void execute(String[] args) {
    try {
      Ticket newTicket = TicketGenerator.createTicket(venueManager);
      collectionManager.addTicket(newTicket);
      TextColor.successMessage("Билет успешно добавлен:");
      System.out.println(newTicket);

    } catch (Exception e) {
      TextColor.errorMessage("Ошибка при добавлении билета: ");
      System.out.println(e.getMessage());
    }
  }

  @Override
  public String getDescription() {
    return "добавляет билет в коллекцию";
  }
}
