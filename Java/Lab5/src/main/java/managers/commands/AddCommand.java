package managers.commands;

import data.*;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import managers.CollectionManager;
import system.InputManager;
import system.TextColor;

/**
 * Класс команды добавляющей и затем валидирующей новый билет
 *
 * @author Maks
 * @version 1.0
 */
public class AddCommand implements Command {
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  @Override
  public void execute(String[] args) {
    try {
      System.out.print("Введите название билета: ");
      String name = InputManager.nextLine().trim();
      if (name.isEmpty()) throw new IllegalArgumentException("Название не может быть пустым");

      System.out.print("Введите координату X: ");
      Double coordX = Double.parseDouble(InputManager.nextLine());

      System.out.print("Введите координату Y (> -593): ");
      Integer coordY = Integer.parseInt(InputManager.nextLine());

      Coordinates coordinates = new Coordinates(coordX, coordY);

      System.out.print("Введите цену (> 0): ");
      Double price = Double.parseDouble(InputManager.nextLine());

      System.out.print("Введите тип билета (VIP, USUAL, BUDGETARY, CHEAP): ");
      TicketType ticketType = TicketType.valueOf(InputManager.nextLine().trim().toUpperCase());

      System.out.print("Введите название площадки: ");
      String venueName = InputManager.nextLine().trim();
      if (venueName.isEmpty())
        throw new IllegalArgumentException("Название площадки не может быть пустым");

      System.out.print("Введите вместимость площадки (> 0): ");
      int capacity = Integer.parseInt(InputManager.nextLine());

      System.out.print("Введите тип площадки (PUB, BAR, LOFT, CINEMA, STADIUM): ");
      String venueTypeStr = InputManager.nextLine().trim();
      VenueType venueType =
          venueTypeStr.isEmpty() ? null : VenueType.valueOf(venueTypeStr.toUpperCase());

      System.out.print("Введите zipCode: ");
      String zipCode = InputManager.nextLine().trim();
      if (zipCode.isEmpty()) throw new IllegalArgumentException("zipCode не может быть пустым");

      System.out.print("Введите название города: ");
      String townName = InputManager.nextLine().trim();
      if (townName.isEmpty()) throw new IllegalArgumentException("Город не может быть пустым");

      System.out.print("Введите координату X города: ");
      long townX = Long.parseLong(InputManager.nextLine());

      System.out.print("Введите координату Y города: ");
      Long townY = Long.parseLong(InputManager.nextLine());

      Location town = new Location(townX, townY, townName);
      Address address = new Address(zipCode, town);
      Venue venue = new Venue(venueName, capacity, venueType, address);

      Ticket newTicket = new Ticket(name, coordinates, price, ticketType, venue);

      Set<ConstraintViolation<Ticket>> violations = validator.validate(newTicket);
      if (!violations.isEmpty()) {
        StringBuilder errorMessage = new StringBuilder("Ошибки валидации:\n");
        for (ConstraintViolation<Ticket> violation : violations) {
          errorMessage.append("- ").append(violation.getMessage()).append("\n");
        }
        throw new IllegalArgumentException(errorMessage.toString());
      }

      CollectionManager.addTicket(newTicket);
      System.out.println(
          TextColor.ANSI_GREEN + "Билет успешно добавлен: " + newTicket + TextColor.ANSI_RESET);

    } catch (Exception e) {
      System.out.println(
          TextColor.ANSI_RED
              + "Ошибка при добавлении билета: "
              + e.getMessage()
              + TextColor.ANSI_RESET);
    }
  }
}
