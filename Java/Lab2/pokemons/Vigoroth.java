package pokemons;
import ru.ifmo.se.pokemon.*;
import attacks.status.*;
import attacks.special.*;
import attacks.physical.*;
public class Vigoroth extends Slakoth {
    public  Vigoroth(){
        this("Безымянный",1);
    }

    public Vigoroth(String name,int level){
        super(name,level);
        this.setType(Type.NORMAL);
        this.setStats(80,80,80,55,55,90);
        this.setMove(new Slash());
    }
}
