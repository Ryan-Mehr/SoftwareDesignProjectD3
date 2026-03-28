package Factory;

import Classes.Heros.Chaos;
import Classes.Heros.Hero;
import Interfaces.Factory.HeroFactory;

public class ChaosFactory implements HeroFactory {
    @Override
    public Hero createHero() {
        return new Chaos();
    }
}
