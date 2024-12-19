package pokemons;
import ru.ifmo.se.pokemon.*;
import attacks.status.*;
import attacks.special.*;

public class TapuBulu extends Pokemon{
    public  TapuBulu(){
        this("Безымянный",1);
    }

    public TapuBulu(String name,int level){
        super(name,level);
        this.setType(Type.GRASS, Type.FAIRY);
        this.setStats(70,130,115,85,95,75);
        this.setMove(new DoubleTeam(),new Swagger(),new NatureMadness(), new Psywave());
    }
}
