package Factory;

import Classes.Heros.Hero;
import Classes.Heros.Mage;
import Interfaces.Factory.HeroFactory;

public class MageFactory implements HeroFactory {
    @Override
    public Hero createHero() {
        return new Mage();
    }
}
