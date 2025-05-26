package managers.commands;

import data.Ticket;
import data.generators.TicketGenerator;
import managers.CollectionManager;
import managers.VenueManager;

/**
 * Класс команды добавляющую билет, если вместимость площадки нового билета больше чем максимальная
 * вместимость среди билетов в коллекции
 *
 * @author Maks
 * @version 1.0
 */
public class AddIfMaxCommand implements Command {
  private final CollectionManager collectionManager;
  private final VenueManager venueManager;

  public AddIfMaxCommand(CollectionManager collectionManager, VenueManager venueManager) {
    this.collectionManager = collectionManager;
    this.venueManager = venueManager;
  }

  @Override
  public void execute(String[] args) {
    if (args.length != 1) {
      System.out.println("Ошибка: данная команда не принимает аргументы");
      return;
    }

    Ticket maxTicket = collectionManager.getMaxTicket();
    Ticket ticket = TicketGenerator.createTicket(venueManager);

    if (maxTicket == null || collectionManager.getDequeCollection().isEmpty()) {
      collectionManager.addTicket(ticket);
    } else if (ticket.compareTo(maxTicket) > 0) {
      collectionManager.addTicket(ticket);
    }
  }

  @Override
  public String getDescription() {
    return "добавляет билет если он максимальный";
  }
}
