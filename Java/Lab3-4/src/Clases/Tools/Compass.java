package Clases.Tools;
import Enums.Directions;
import  Interfaces.NavigationTool;
import java.util.Random;
public class Compass implements NavigationTool {
    private boolean working;
    private Directions currentDirection;

    public Compass()
    {
        this.working = new Random().nextBoolean();
        this.currentDirection = Directions.getRandomDirection();
    }

    @Override
    public String getToolType() {
        return "Compass";
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    public String getDirection() {
        return currentDirection.toString();
    }

}
