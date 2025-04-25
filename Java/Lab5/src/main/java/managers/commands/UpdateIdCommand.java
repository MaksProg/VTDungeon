package managers.commands;

import data.*;
import data.Ticket;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import managers.CollectionManager;
import system.InputManager;
import system.TextColor;

/**
 * Класс реализующий команду обновления элемента коллекции по айди.
 *
 * @author Maks
 * @version 1.0
 */
public class UpdateIdCommand implements Command {
  private static final Validator validator =
      Validation.buildDefaultValidatorFactory().getValidator();

  @Override
  public void execute(String[] args) {
    if (args.length == 0) {
      System.out.println("Укажите id билета для обновления");
    }

    try {
      int id = Integer.parseInt(args[0]);
      Optional<Ticket> existingTicket =
          CollectionManager.getDequeCollection().stream().filter(t -> t.getId() == id).findFirst();
      if (existingTicket.isEmpty()) {
        System.out.println(
            TextColor.ANSI_RED + "Билет с id " + id + " не найден" + TextColor.ANSI_RESET);
        return;
      }

      Ticket ticket = existingTicket.get();
      Scanner scanner = InputManager.getScanner();

      System.out.print("Введите новое название билета: ");
      ticket.setName(scanner.nextLine().trim());

      System.out.print("Введите координату X: ");
      Double coordX = Double.parseDouble(scanner.nextLine());

      System.out.print("Введите координату Y (> -593): ");
      Integer coordY = Integer.parseInt(scanner.nextLine());

      ticket.setCoordinates(new Coordinates(coordX, coordY));

      System.out.print("Введите цену (> 0): ");
      ticket.setPrice(Double.parseDouble(scanner.nextLine()));

      System.out.print("Введите тип билета (VIP, USUAL, BUDGETARY, CHEAP): ");
      ticket.setType(TicketType.valueOf(scanner.nextLine().trim().toUpperCase()));

      System.out.print("Введите название площадки: ");
      String venueName = scanner.nextLine().trim();

      System.out.print("Введите вместимость площадки (> 0): ");
      int capacity = Integer.parseInt(scanner.nextLine());

      System.out.print("Введите тип площадки (PUB, BAR, LOFT, CINEMA, STADIUM): ");
      String venueTypeStr = scanner.nextLine().trim();
      VenueType venueType =
          venueTypeStr.isEmpty() ? null : VenueType.valueOf(venueTypeStr.toUpperCase());

      System.out.print("Введите zipCode: ");
      String zipCode = scanner.nextLine().trim();

      System.out.print("Введите название города: ");
      String townName = scanner.nextLine().trim();

      System.out.print("Введите координату X города: ");
      long townX = Long.parseLong(scanner.nextLine());

      System.out.print("Введите координату Y города: ");
      Long townY = Long.parseLong(scanner.nextLine());

      Location town = new Location(townX, townY, townName);
      Address address = new Address(zipCode, town);
      Venue venue = new Venue(venueName, capacity, venueType, address);

      ticket.setVenue(venue);

      Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
      if (!violations.isEmpty()) {
        System.out.println(TextColor.ANSI_RED + "Ошибки валидации:" + TextColor.ANSI_RESET);
        for (ConstraintViolation<Ticket> violation : violations) {
          System.out.println("- " + violation.getMessage());
        }
        return;
      }

      System.out.println(
          TextColor.ANSI_GREEN + "Билет с id " + id + " успешно обновлён." + TextColor.ANSI_RESET);

    } catch (Exception e) {
      System.out.println(
          TextColor.ANSI_RED
              + "Ошибка при обновлении билета: "
              + e.getMessage()
              + TextColor.ANSI_RESET);
    }
  }
}
