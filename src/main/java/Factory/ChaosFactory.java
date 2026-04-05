package Factory;

import Classes.Heros.Chaos;
import Classes.Heros.Hero;
import Interfaces.Factory.HeroFactory;

public class ChaosFactory implements HeroFactory {
    private static volatile ChaosFactory obj = null;
    private ChaosFactory() {
        System.out.println("ChaosFactory is instantiated.");
    }
    public static ChaosFactory getInstance() {
        if (obj == null) {
            synchronized (ChaosFactory.class) {
                if (obj == null) {
                    obj = new ChaosFactory();
                }
            }
        }
        return obj;
    }
    @Override
    public Hero createHero() {
        return new Chaos();
    }
}
