package common.system.utils;

import common.data.*;
import common.system.InputManager;
import java.util.Scanner;

/** Утилитный класс для чтения объектов Ticket. */
public class TicketReader {

  /**
   * Читает объект Ticket с помощью указанного сканера.
   *
   * @param in Сканер, из которого читается ввод
   * @return Новый объект Ticket
   */
  public static Ticket readTicket(Scanner in) {
    String name =
        InputManager.promptValid(
            in,
            "Введите название билета: ",
            s -> s,
            s -> !s.isEmpty(),
            "Название не может быть пустым");

    Double coordX =
        InputManager.promptValid(
            in,
            "Введите координату X: ",
            Double::parseDouble,
            x -> true,
            "Некорректное значение X");

    Integer coordY =
        InputManager.promptValid(
            in,
            "Введите координату Y (> -593): ",
            Integer::parseInt,
            y -> y > -593,
            "Y должен быть больше -593");

    Coordinates coordinates = new Coordinates(coordX, coordY);

    double price =
        InputManager.promptValid(
            in,
            "Введите цену (> 0): ",
            Double::parseDouble,
            p -> p > 0,
            "Цена должна быть больше 0");

    TicketType type =
        InputManager.promptValid(
            in,
            "Введите тип билета (VIP, USUAL, BUDGETARY, CHEAP): ",
            s -> TicketType.valueOf(s.toUpperCase()),
            t -> true,
            "Неверный тип билета");

    String venueName =
        InputManager.promptValid(
            in,
            "Введите название площадки: ",
            s -> s,
            s -> !s.isEmpty(),
            "Название не может быть пустым");

    int capacity =
        InputManager.promptValid(
            in,
            "Введите вместимость площадки (> 0): ",
            Integer::parseInt,
            c -> c > 0,
            "Вместимость должна быть больше 0");

    String venueTypeStr =
        InputManager.promptValid(
            in,
            "Введите тип площадки (PUB, BAR, LOFT, CINEMA, STADIUM) или оставьте пустым: ",
            s -> s,
            s -> true,
            "Тип площадки не обязателен");

    VenueType venueType =
        venueTypeStr.isEmpty() ? null : VenueType.valueOf(venueTypeStr.toUpperCase());

    String zipCode =
        InputManager.promptValid(
            in, "Введите zipCode: ", s -> s, s -> !s.isEmpty(), "zipCode не может быть пустым");

    String townName =
        InputManager.promptValid(
            in,
            "Введите название города: ",
            s -> s,
            s -> !s.isEmpty(),
            "Название города не может быть пустым");

    long townX =
        InputManager.promptValid(
            in,
            "Введите координату X города: ",
            Long::parseLong,
            x -> true,
            "Некорректное значение X");

    Long townY =
        InputManager.promptValid(
            in,
            "Введите координату Y города: ",
            Long::parseLong,
            y -> true,
            "Некорректное значение Y");

    Location town = new Location(townX, townY, townName);
    Address address = new Address(zipCode, town);
    Venue venue = new Venue(venueName, capacity, venueType, address);

    return new Ticket(name, coordinates, price, type, venue);
  }
}
