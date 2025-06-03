package common.data.generators;

import common.data.Address;
import common.data.Location;
import common.data.Venue;
import common.data.VenueType;
import common.system.InputManager;
import java.util.Scanner;

public class VenueGenerator {
  public static Venue createVenue(Scanner in) {
    String venueName =
        InputManager.promptValid(
            in,
            "Введите название площадки: ",
            s -> s,
            s -> !s.isEmpty(),
            "Название площадки не может быть пустым");

    int capacity =
        InputManager.promptValid(
            in,
            "Введите вместимость площадки (> 0): ",
            Integer::parseInt,
            c -> c > 0,
            "Вместимость должна быть больше 0");

    VenueType venueType =
        InputManager.promptValid(
            in,
            "Введите тип площадки (PUB, BAR, LOFT, CINEMA, STADIUM) или оставьте пустым: ",
            s -> s.isEmpty() ? null : VenueType.valueOf(s.toUpperCase()),
            v -> true,
            "Недопустимый тип площадки");

    String zipCode =
        InputManager.promptValid(
            in, "Введите zipCode: ", s -> s, s -> !s.isEmpty(), "zipCode не может быть пустым");

    String townName =
        InputManager.promptValid(
            in,
            "Введите название города: ",
            s -> s,
            s -> !s.isEmpty(),
            "Город не может быть пустым");

    long townX =
        InputManager.promptValid(
            in, "Введите координату X города: ", Long::parseLong, x -> true, "Некорректное число");

    Long townY =
        InputManager.promptValid(
            in, "Введите координату Y города: ", Long::parseLong, y -> true, "Некорректное число");

    Location town = new Location(townX, townY, townName);
    Address address = new Address(zipCode, town);
    Venue venue = new Venue(venueName, capacity, venueType, address);

    System.out.println("Площадка создана");
    return venue;
  }
}
