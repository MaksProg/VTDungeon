package common.data.generators;

import common.data.*;
import common.managers.VenueManager;
import common.system.InputManager;
import java.util.Scanner;

public class TicketGenerator {
  public static Ticket createTicket(VenueManager venueManager, Scanner in) {
    String name =
        InputManager.promptValid(
            in,
            "Введите название билета: ",
            s -> s,
            s -> !s.isEmpty(),
            "Название не может быть пустым");

    Double coordX =
        InputManager.promptValid(
            in, "Введите координату X: ", Double::parseDouble, x -> true, "Некорректное число");

    Integer coordY =
        InputManager.promptValid(
            in,
            "Введите координату Y (> -593): ",
            Integer::parseInt,
            y -> y > -593,
            "Координата Y должна быть больше -593");

    Coordinates coordinates = new Coordinates(coordX, coordY);

    Double price =
        InputManager.promptValid(
            in,
            "Введите цену (> 0): ",
            Double::parseDouble,
            p -> p > 0,
            "Цена должна быть больше 0");

    TicketType ticketType =
        InputManager.promptValid(
            in,
            "Введите тип билета (VIP, USUAL, BUDGETARY, CHEAP): ",
            s -> TicketType.valueOf(s.toUpperCase()),
            t -> true,
            "Недопустимый тип билета");

    Venue venue = promptVenueChoice(venueManager, in);

    Ticket newTicket = new Ticket(name, coordinates, price, ticketType, venue);
    System.out.println("Билет создан");
    return newTicket;
  }

  private static Venue promptVenueChoice(VenueManager venueManager, Scanner in) {
    while (true) {
      System.out.println(
          """
        Выберите способ указания площадки:
        1. Не указывать площадку (null)
        2. Создать новую площадку
        3. Выбрать из существующих по ID
        """);

      if (!InputManager.hasNextLine()) {
        throw new IllegalStateException("Ожидался ввод, но данные закончились.");
      }

      String choice = InputManager.nextLine().trim();
      System.out.println(choice);

      switch (choice) {
        case "1":
          return null;

        case "2":
          Venue newVenue = VenueGenerator.createVenue(in);
          venueManager.addVenue(newVenue);
          System.out.println("Создана новая площадка с ID: " + newVenue.getId());
          return newVenue;

        case "3":
          System.out.print("Введите ID площадки: ");
          if (!InputManager.hasNextLine()) {
            throw new IllegalStateException("Ожидался ввод ID площадки, но данные закончились.");
          }

          String idInput = InputManager.nextLine().trim();
          System.out.println(idInput);

          try {
            int id = Integer.parseInt(idInput);
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
