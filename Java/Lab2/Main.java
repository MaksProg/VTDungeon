import ru.ifmo.se.pokemon.*;
import pokemons.*;

public class Main {
    public static void main(String[] args) {
            Battle b = new Battle();
            TapuBulu p1 = new TapuBulu("Бык",2);
            Wooper p2 = new Wooper("Конёк",2);
            Quagsire p3 = new Quagsire("Дино",3);
            Slakoth p4 = new Slakoth("Скуфёнок",3);
            Vigoroth p5 = new Vigoroth("Скуф",5);
            Slaking  p6 = new Slaking("Скуфендуй",4);
            b.addAlly(p1);
            b.addAlly(p2);
            b.addAlly(p3);
            b.addFoe(p4);
            b.addFoe(p5);
            b.addFoe(p6);
            b.go();
    }
    }
