package pokemons;
import ru.ifmo.se.pokemon.*;
import attacks.status.*;
import attacks.special.*;
import attacks.physical.*;
public class Slaking extends Vigoroth {
    public  Slaking(){
        this("Безымянный",1);
    }

    public Slaking(String name,int level){
        super(name,level);
        this.setType(Type.NORMAL);
        this.setStats(150,160,100,95,65,100);
        this.setMove(new Bulldoze());
    }
}
