package common.data.generators;

import common.data.*;
import common.system.InputManager;
import java.util.Scanner;

public class TicketGenerator {
  public static Ticket createTicket(Scanner in) {
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

    Venue venue = promptVenueChoice(in);

    return new Ticket(name, coordinates, price, ticketType, venue);
  }

  private static Venue promptVenueChoice(Scanner in) {
    while (true) {
      System.out.println(
          """
          Выберите способ указания площадки:
          1. Не указывать площадку (null)
          2. Создать новую площадку
          3. Использовать существующую площадку по ID
          """);

      if (!InputManager.hasNextLine()) {
        throw new IllegalStateException("Ожидался ввод, но данные закончились.");
      }

      String choice = InputManager.safeNextLine().trim();

      switch (choice) {
        case "1":
          return null;

        case "2":
          return VenueGenerator.createVenue(in);

        case "3":
          System.out.print("Введите ID существующей площадки: ");
          if (!InputManager.hasNextLine()) {
            throw new IllegalStateException("Ожидался ввод ID площадки, но данные закончились.");
          }

          String idInput = InputManager.safeNextLine().trim();

          try {
            int id = Integer.parseInt(idInput);
            Venue venueWithId = new Venue();
            venueWithId.setId(id);
            return venueWithId;
          } catch (NumberFormatException e) {
            System.out.println("Некорректный формат ID. Введите целое число.");
          }
          break;

        default:
          System.out.println("Некорректный выбор. Попробуйте снова.");
      }
    }
  }
}
