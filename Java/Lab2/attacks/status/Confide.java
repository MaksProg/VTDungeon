package attacks.status;
import ru.ifmo.se.pokemon.*;
public class Confide extends StatusMove {
    public Confide()
    {
        super(Type.NORMAL,0,0);
    }
    @Override
    protected String describe(){
        return "Использует атаку Confide";
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setCondition(new Effect().stat(Stat.SPECIAL_ATTACK,-1));
    }
}
