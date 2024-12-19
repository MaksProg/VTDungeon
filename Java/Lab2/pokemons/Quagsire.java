package pokemons;
import ru.ifmo.se.pokemon.*;
import attacks.special.*;
import attacks.status.*;
import attacks.physical.*;
public class Quagsire extends Wooper {
    public  Quagsire(){
        this("Безымянный",1);
    }

    public Quagsire(String name,int level){
        super(name,level);
        this.setType(Type.WATER, Type.GROUND);
        this.setStats(95,85,85,65,65,35);
        this.setMove(new RockTomb());
    }
}
