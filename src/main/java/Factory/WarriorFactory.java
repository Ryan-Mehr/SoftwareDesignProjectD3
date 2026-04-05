package Factory;

import Classes.Heros.Hero;
import Classes.Heros.Warrior;

public class WarriorFactory implements Interfaces.Factory.HeroFactory {
    private static volatile WarriorFactory obj = null;
    private WarriorFactory() {
        System.out.println("WarriorFactory is instantiated.");
    }
    public static WarriorFactory getInstance() {
        if (obj == null) {
            synchronized (WarriorFactory.class) {
                if (obj == null) {
                    obj = new WarriorFactory();
                }
            }
        }
        return obj;
    }

    @Override
    public Hero createHero() {
        return new Warrior();
    }
    // https://www.geeksforgeeks.org/system-design/factory-method-for-designing-pattern/
}
