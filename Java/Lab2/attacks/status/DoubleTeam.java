package attacks.status;
import ru.ifmo.se.pokemon.*;
public class DoubleTeam extends StatusMove{
    public DoubleTeam(){
        super(Type.NORMAL,0,0);
    }
    @Override
    protected String describe(){
        return "Использует атаку Double Team";
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.EVASION,+1);
    }
}
