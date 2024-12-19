package pokemons;
import ru.ifmo.se.pokemon.*;
import attacks.status.*;
import attacks.special.*;
import attacks.physical.*;
public class Slakoth extends Pokemon{
    public  Slakoth(){
        this("Безымянный",1);
    }

    public Slakoth(String name,int level){
        super(name,level);
        this.setType(Type.NORMAL);
        this.setStats(60,60,60,35,35,30);
        this.setMove(new RockSlide());
    }
}
