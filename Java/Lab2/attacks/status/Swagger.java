package attacks.status;
import ru.ifmo.se.pokemon.*;

public class Swagger extends StatusMove {
    public Swagger()
    {
        super(Type.NORMAL,0,100);
    }
    @Override
    protected String describe(){
        return "Использует атаку Swagger";
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        if (Math.random() <=0.33){
            Effect.confuse(p);
        }
    }
}
