package attacks.physical;
import ru.ifmo.se.pokemon.*;
public class RockSlide extends PhysicalMove{
    public RockSlide(){
        super(Type.ROCK,75,90);
    }
    @Override
    protected String describe(){
        return "Использует атаку Rock Slide";
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        if (Math.random()<=0.3)
        {
            Effect.flinch(p);
        }
    }
}
