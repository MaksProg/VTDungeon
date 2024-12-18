package Enums;

import java.util.Random;

public enum Landscapes {
    PLAIN("на равнину"),
    MOUNTAIN("на гору"),
    ISLAND("на остров");

    public static Landscapes getRandomLandscape() {
        Landscapes[] values = Landscapes.values();
        return values[new Random().nextInt(values.length)];
    }

    private final String description;

    Landscapes(String description){
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
