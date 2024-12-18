package Clases.charachters;
import Records.ResearchResult;
import Enums.Specializations;
import Exceptions.NoResearchResultException;

import java.util.Objects;
import java.util.Random;


public class Professor extends Human{
    private final Specializations specialization;

    public Professor(String name, Specializations specialization){
        super(name);
        this.specialization = specialization;
    }

    public String getName()
    {
        return "Имя профессора:" + this.name;
    }

    public String getSpecialization()
    {
        return"(" + this.specialization+ ")";
    }

    public ResearchResult conductResearch() throws NoResearchResultException{
        Random random = new Random();
        int chance = random.nextInt(100);
        if (chance < 30){
            throw  new NoResearchResultException(getName() +"("+this.specialization+")"+ " провёл исследование, но ничего не нашёл");
        }

        String result;
        switch (specialization){
            case GEOLOGIST -> result = "Нашёл редкие минералы.";
            case BIOLOGIST -> result = "Нашёл новый вид растений";
            case PHYSICIST -> result = "Cобрал данные о погоде";
            default -> result = "Неизвестные результаты";
        }
        return new ResearchResult(getName(),specialization,result);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Professor professor = (Professor) obj;
        return Objects.equals(name, professor.name) && specialization == professor.specialization;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, specialization);
    }

    @Override
    public String toString() {
        return "Professor{Имя='" + name + "', специализация=" + specialization + '}';
    }

}
