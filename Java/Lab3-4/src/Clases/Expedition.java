package Clases;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import Clases.Tools.Compass;
import Clases.Tools.Sextant;
import Clases.charachters.Assistant;
import Clases.charachters.Professor;
import Enums.Directions;
import Enums.WeatherEvents;
import Enums.Landscapes;
import Exceptions.NoResearchResultException;
import Records.ResearchResult;

public class Expedition {
    Compass compass = new Compass();
    Sextant sextant = new Sextant();
    private final List<Professor> professors;
    private final List<Assistant> assistants;
    private final List<ResearchResult> results;
    private Landscapes landscape;
    private WeatherEvents weather;

    public Expedition(List<Professor> professors, List<Assistant> assistants) {
        this.professors = professors;
        this.assistants = assistants;
        this.results = new ArrayList<>();
    }

    public void startExpedition(){
        weather = WeatherEvents.getRandomWeather();
        System.out.println("Мы отплыли в "+ weather+".");
        System.out.println("Наш отряд состоял из профессоров:");
        for (Professor professor:professors){
            System.out.println("- " + professor.getName() + " " + professor.getSpecialization());
        }
        System.out.println("и ассистентов:");
        for (Assistant assistant:assistants)
        {
            System.out.println("- "+ assistant.getName() + " " + assistant.getSkills());
        }
        this.landscape = Landscapes.getRandomLandscape();
        if (compass.isWorking()) {
            System.out.println("Использовав компас, я понял что мы идём " + compass.getDirection()+ ", имеем координаты " +sextant.getLatitude()+ " " +sextant.getLongitude()+" и приближаемся к прибытию " + landscape);
        }
        if (!compass.isWorking()){
            System.out.println("Использовав компас, я понял что мы идём " + compass.getDirection()+ ", имеем координаты " +sextant.getLatitude()+ " " +sextant.getLongitude()+" и приближаемся к прибытию " + landscape);
            this.landscape = Landscapes.getRandomLandscape();
        }
        System.out.println("Прибыв " + this.landscape + " мы разбили лагерь и на следующий день начали исследования.");
    }


    public String conductResearch(){
        StringBuilder researchSummary = new StringBuilder("Результаты исследований:\n");
        for (Professor professor : professors) {
            try {
                ResearchResult result = professor.conductResearch();
                results.add(result);
                researchSummary.append("- ").append(result.getSummary()).append("\n");
            } catch (NoResearchResultException e) {
                // Обработка случая отсутствия результатов
                researchSummary.append("- ").append(e.getMessage()).append("\n");
            }
        }

        if (results.isEmpty()) {
            researchSummary.append("Ни одно исследование не принесло результата.\n");
        }

        return researchSummary.toString();
    }

    public void wrapUp(){
        this.weather = WeatherEvents.getRandomWeather();
        System.out.println("После проведения всех исследований, мы разобрали лагерь и отправились домой в " + this.weather);
        System.out.println("Экспедиция закончена");
    }
}
