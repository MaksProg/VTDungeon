package attacks.special;
import ru.ifmo.se.pokemon.*;
public class NatureMadness extends SpecialMove{
    public NatureMadness(){
        super(Type.FAIRY,0,90);
    }
    @Override
    protected String describe(){
        return "Использует атаку Nature`s Madness";
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.HP,-2);
    }
}
