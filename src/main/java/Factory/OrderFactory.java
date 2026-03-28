package Factory;

import Classes.Heros.Hero;
import Classes.Heros.OrderHero;
import Interfaces.Factory.HeroFactory;

public class OrderFactory implements HeroFactory {
    @Override
    public Hero createHero() {
        return new OrderHero();
    }
}
