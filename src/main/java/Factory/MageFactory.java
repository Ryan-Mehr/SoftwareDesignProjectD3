package Factory;

import Classes.Heros.Hero;
import Classes.Heros.Mage;
import Interfaces.Factory.HeroFactory;

public class MageFactory implements HeroFactory {
    private static volatile MageFactory obj = null;
    private MageFactory() {
        System.out.println("MageFactory is instantiated.");
    }
    public static MageFactory getInstance() {
        if (obj == null) {
            synchronized (MageFactory.class) {
                if (obj == null) {
                    obj = new MageFactory();
                }
            }
        }
        return obj;
    }

    @Override
    public Hero createHero() {
        return new Mage();
    }
}
