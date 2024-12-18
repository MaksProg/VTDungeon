package Clases.charachters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Assistant extends Human{
    private boolean canFly;
    private boolean canUseRadio;
    private List<String> skills;

    public Assistant(){
        this.name = "Безымянный";
        this.canFly = false;
        this.canUseRadio = false;
    }

    public Assistant(String name, boolean canFly, boolean canUseRadio)
    {
        this.name = name;
        this.canFly = canFly;
        this.canUseRadio = canUseRadio;
        this.skills = new ArrayList<>();
        if (canUseRadio)
        {
            this.addSkill("Умеет пользоваться радио");
        }
        if (canFly)
        {
            this.addSkill("Умеет управлять самолётом");
        }
    }

    public String getName(){
        return "Имя ассистента:"+this.name;
    }

    public List<String> getSkills() {
        return Collections.unmodifiableList(skills);
    }

    public void addSkill(String skill) {
        if (skill == null || skill.isEmpty()) {
            throw new IllegalArgumentException("Skill cannot be null or empty.");
        }
        if (!skills.contains(skill)) { // Проверка на уникальность
            skills.add(skill);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Assistant assistant = (Assistant) obj;
        return Objects.equals(name, assistant.name) && Objects.equals(skills, assistant.skills);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, getSkills());
    }

    @Override
    public String toString() {
        return "Assistant{Имя='" + name + "', Навыки=" + getSkills() + '}';
    }
}
