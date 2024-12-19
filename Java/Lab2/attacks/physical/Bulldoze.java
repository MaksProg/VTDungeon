package attacks.physical;

import ru.ifmo.se.pokemon.*;
public class Bulldoze extends PhysicalMove {
    public Bulldoze(){
        super(Type.GROUND,60,100);

    }
    @Override
    protected String describe(){
        return "Использует атаку Bulldoze";
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.SPEED,-1);
    }
}
