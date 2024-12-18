package Enums;
import java.util.Random;
public enum Directions {
    NORTH("на север"),
    SOUTH("на юг"),
    EAST("на восток"),
    WEST("на запад");

    public static Directions getRandomDirection() {
        Directions[] values = Directions.values();
        return values[new Random().nextInt(values.length)];
    }

    private final String description;

    Directions(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
