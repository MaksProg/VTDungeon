package Clases.Tools;
import Interfaces.NavigationTool;
import java.util.Random;
public class Sextant implements NavigationTool{
    private boolean working;
    private float latitude;
    private float longitude;
    private Random random = new Random();
    public Sextant() {
        this.working = true;
        this.latitude = random.nextFloat()*180;
        this.longitude = random.nextFloat()*180;
    }

    @Override
    public String getToolType() {
        return "Секстант";
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    @Override
    public String getDirection() {
        return "Секстант показывает на позицию солнца.";
    }

    public String getLatitude(){
        return "широта:" + latitude;

    }

    public String getLongitude(){
        return "долгота:" + longitude;
    }

}
