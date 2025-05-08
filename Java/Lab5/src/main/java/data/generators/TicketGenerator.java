package data.generators;

import data.*;
import managers.VenueManager;
import system.InputManager;

/**
 * Класс реализующий создание билета
 * @author Maks
 * @version 1.0
 */
public class TicketGenerator {
  public static Ticket createTicket(VenueManager venueManager) {
    String name =
        InputManager.promptValid(
            "Введите название билета: ",
            s -> s,
            s -> !s.isEmpty(),
            "Название не может быть пустым");

    Double coordX =
        InputManager.promptValid(
            "Введите координату X: ", Double::parseDouble, x -> true, "Некорректное число");

    Integer coordY =
        InputManager.promptValid(
            "Введите координату Y (> -593): ",
            Integer::parseInt,
            y -> y > -593,
            "Координата Y должна быть больше -593");

    Coordinates coordinates = new Coordinates(coordX, coordY);

    Double price =
        InputManager.promptValid(
            "Введите цену (> 0): ", Double::parseDouble, p -> p > 0, "Цена должна быть больше 0");

    TicketType ticketType =
        InputManager.promptValid(
            "Введите тип билета (VIP, USUAL, BUDGETARY, CHEAP): ",
            s -> TicketType.valueOf(s.toUpperCase()),
            t -> true,
            "Недопустимый тип билета");

    Venue venue = promptVenueChoice(venueManager);

    Ticket newTicket = new Ticket(name, coordinates, price, ticketType, venue);
    System.out.println("Билет создан");
    return newTicket;
  }

  private static Venue promptVenueChoice(VenueManager venueManager) {
    while (true) {
      System.out.println(
          """
                                    Выберите способ указания площадки:
                                    1. Не указывать площадку (null)
                                    2. Создать новую площадку
                                    3. Выбрать из существующих по ID
                                """);
      String choice = InputManager.nextLine().trim();

      switch (choice) {
        case "1":
          return null;
        case "2":
          Venue newVenue = VenueGenerator.createVenue();
          venueManager.addVenue(newVenue);
          System.out.println("Создана новая площадка с ID: " + newVenue.getId());
          return newVenue;
        case "3":
          System.out.print("Введите ID площадки: ");
          try {
            int id = Integer.parseInt(InputManager.nextLine().trim());
            Venue existing = venueManager.getById(id);
            if (existing != null) return existing;
            else System.out.println("Площадка с таким ID не найдена.");
          } catch (NumberFormatException e) {
            System.out.println("Некорректный формат ID.");
          }
          break;
        default:
          System.out.println("Некорректный выбор. Попробуйте снова.");
      }
    }
  }
}
