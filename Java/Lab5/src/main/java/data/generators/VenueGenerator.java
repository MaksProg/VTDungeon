package data.generators;

import data.Address;
import data.Location;
import data.Venue;
import data.VenueType;
import system.InputManager;

/**
 * Класс реализующий создание Venue
 *
 * @author Maks
 * @version 1.0
 */
public class VenueGenerator {
  public static Venue createVenue() {
    String venueName =
        InputManager.promptValid(
            "Введите название площадки: ",
            s -> s,
            s -> !s.isEmpty(),
            "Название площадки не может быть пустым");

    int capacity =
        InputManager.promptValid(
            "Введите вместимость площадки (> 0): ",
            Integer::parseInt,
            c -> c > 0,
            "Вместимость должна быть больше 0");

    VenueType venueType =
        InputManager.promptValid(
            "Введите тип площадки (PUB, BAR, LOFT, CINEMA, STADIUM) или оставьте пустым: ",
            s -> s.isEmpty() ? null : VenueType.valueOf(s.toUpperCase()),
            v -> true,
            "Недопустимый тип площадки");

    String zipCode =
        InputManager.promptValid(
            "Введите zipCode: ", s -> s, s -> !s.isEmpty(), "zipCode не может быть пустым");

    String townName =
        InputManager.promptValid(
            "Введите название города: ", s -> s, s -> !s.isEmpty(), "Город не может быть пустым");

    long townX =
        InputManager.promptValid(
            "Введите координату X города: ", Long::parseLong, x -> true, "Некорректное число");

    Long townY =
        InputManager.promptValid(
            "Введите координату Y города: ", Long::parseLong, y -> true, "Некорректное число");

    Location town = new Location(townX, townY, townName);
    Address address = new Address(zipCode, town);
    Venue venue = new Venue(venueName, capacity, venueType, address);

    return venue;
  }
}
