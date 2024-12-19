package attacks.special;
import ru.ifmo.se.pokemon.*;
public class Psywave extends SpecialMove {
    public Psywave()
    {
        super(Type.PSYCHIC,0,100);
    }
    @Override
    protected String describe(){
        return "Использует атаку Psywave";
    }
    @Override
    protected double calcRandomDamage(Pokemon att, Pokemon def){
        return att.getLevel()*(0.5+Math.random());
    }

}
