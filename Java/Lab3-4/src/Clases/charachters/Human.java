package Clases.charachters;

public abstract class Human {
    protected String name;

    public Human(){
        this.name = "Безымянный";
    }

    public Human(String name){
        this.name = name;
    }

    public abstract String getName();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Human human = (Human) obj;
        return getName().equals(human.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Human{name='" + getName() + "'}";
    }
}
