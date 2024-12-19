package pokemons;
import ru.ifmo.se.pokemon.*;
import attacks.special.*;
import attacks.status.*;
import attacks.physical.*;
public class Wooper extends Pokemon {

    public  Wooper(){
        this("Безымянный",1);
    }

    public Wooper(String name,int level){
        super(name,level);
        this.setType(Type.WATER, Type.GROUND);
        this.setStats(55,45,45,25,25,15);
        this.setMove(new Confide(),new Scald(),new Facade());
    }
}
