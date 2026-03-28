package Factory;

import Classes.Heros.Hero;
import Classes.Heros.Warrior;

public class WarriorFactory implements Interfaces.Factory.HeroFactory {
    @Override
    public Hero createHero() {
        return new Warrior();
    }
    // https://www.geeksforgeeks.org/system-design/factory-method-for-designing-pattern/
}
