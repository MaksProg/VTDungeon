package managers.commands;

import data.*;
import data.Ticket;
import java.util.Optional;
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

  private final CollectionManager collectionManager;

  public UpdateIdCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
  }

  @Override
  public void execute(String[] args) {
    if (args.length == 0) {
      System.out.println("Укажите id билета для обновления");
    }

    try {
      int id = Integer.parseInt(args[0]);
      Optional<Ticket> existingTicket =
          collectionManager.getDequeCollection().stream().filter(t -> t.getId() == id).findFirst();
      if (existingTicket.isEmpty()) {
        System.out.println(
            TextColor.formatError("Билет с id ") + id + TextColor.formatError(" не найден"));
        return;
      }

      Ticket ticket = existingTicket.get();

      String name =
          InputManager.promptValid(
              "Введите новое название билета: ",
              s -> s,
              s -> !s.isEmpty(),
              "Название не может быть пустым");
      ticket.setName(name);

      Double coordX =
          InputManager.promptValid(
              "Введите координату X: ", Double::parseDouble, x -> true, "Некорректное значение X");
      Integer coordY =
          InputManager.promptValid(
              "Введите координату Y (> -593): ",
              Integer::parseInt,
              y -> y > -593,
              "Y должен быть больше -593");
      ticket.setCoordinates(new Coordinates(coordX, coordY));

      double price =
          InputManager.promptValid(
              "Введите цену (> 0): ", Double::parseDouble, p -> p > 0, "Цена должна быть больше 0");
      ticket.setPrice(price);

      TicketType type =
          InputManager.promptValid(
              "Введите тип билета (VIP, USUAL, BUDGETARY, CHEAP): ",
              s -> TicketType.valueOf(s.toUpperCase()),
              t -> true,
              "Неверный тип билета");
      ticket.setType(type);

      String venueName =
          InputManager.promptValid(
              "Введите название площадки: ",
              s -> s,
              s -> !s.isEmpty(),
              "Название не может быть пустым");

      int capacity =
          InputManager.promptValid(
              "Введите вместимость площадки (> 0): ",
              Integer::parseInt,
              c -> c > 0,
              "Вместимость должна быть больше 0");

      String venueTypeStr =
          InputManager.promptValid(
              "Введите тип площадки (PUB, BAR, LOFT, CINEMA, STADIUM): ",
              s -> s,
              s -> true,
              "Тип площадки не обязателен (можно оставить пустым)");
      VenueType venueType =
          venueTypeStr.isEmpty() ? null : VenueType.valueOf(venueTypeStr.toUpperCase());

      String zipCode =
          InputManager.promptValid(
              "Введите zipCode: ", s -> s, s -> !s.isEmpty(), "zipCode не может быть пустым");

      String townName =
          InputManager.promptValid(
              "Введите название города: ",
              s -> s,
              s -> !s.isEmpty(),
              "Название города не может быть пустым");

      long townX =
          InputManager.promptValid(
              "Введите координату X города: ",
              Long::parseLong,
              x -> true,
              "Некорректное значение X");

      Long townY =
          InputManager.promptValid(
              "Введите координату Y города: ",
              Long::parseLong,
              y -> true,
              "Некорректное значение Y");

      Location town = new Location(townX, townY, townName);
      Address address = new Address(zipCode, town);
      Venue venue = new Venue(venueName, capacity, venueType, address);

      ticket.setVenue(venue);

      System.out.println(
          TextColor.formatSuccess("Билет с id ")
              + id
              + TextColor.formatSuccess(" успешно обновлён."));

    } catch (Exception e) {
      TextColor.errorMessage("Ошибка при обновлении билета: ");
      System.out.println(e.getMessage());
    }
  }

  @Override
  public String getDescription() {
    return "обновляет элемент по id";
  }
}
