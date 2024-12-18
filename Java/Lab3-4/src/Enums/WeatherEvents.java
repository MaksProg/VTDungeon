package Enums;

import java.util.Random;

public enum WeatherEvents {
    SUNNY("солнечную погоду"),
    RAINY("дождливую погоду"),
    SNOWY("снежную погоду");

    public static WeatherEvents getRandomWeather() {
        WeatherEvents[] values = WeatherEvents.values();
        return values[new Random().nextInt(values.length)];
    }

    private final String description;

    WeatherEvents(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
