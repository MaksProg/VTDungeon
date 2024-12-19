package attacks.physical;
import ru.ifmo.se.pokemon.*;
public class Slash extends PhysicalMove {
    public Slash(){
        super(Type.NORMAL,70,100);
    }
    @Override
    protected String describe(){
        return "Использует атаку Slash";
    }
    @Override
    protected double calcCriticalHit(Pokemon pokemon, Pokemon pokemon1) {
        if (1./8. > Math.random()) {
            System.out.println("Критический удар!");
            return 2.0;
        }
        else {
            return 1.0;
        }
    }

}
