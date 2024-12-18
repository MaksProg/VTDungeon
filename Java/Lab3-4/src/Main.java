import Clases.Expedition;
import Clases.charachters.Assistant;
import Clases.charachters.Human;
import Clases.charachters.Professor;
import Enums.Specializations;

import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        List<Professor> professors = new ArrayList<>();
        List<Assistant> assistants = new ArrayList<>();
        professors.add(new Professor("Николай", Specializations.GEOLOGIST));
        professors.add(new Professor("Кристоф", Specializations.PHYSICIST));
        professors.add(new Professor("Владислав", Specializations.BIOLOGIST));
        assistants.add(new Assistant("Максим",true,true));

        Expedition expedition = new Expedition(professors, assistants);
        try {
            expedition.startExpedition();
            System.out.println(expedition.conductResearch());
        } catch (Exception e) {
            System.out.println("Ошибка во время экспедиции: " + e.getMessage());
        } finally {
            expedition.wrapUp();
        }
    }
}
