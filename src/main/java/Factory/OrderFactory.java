package Factory;

import Classes.Heros.Hero;
import Classes.Heros.OrderHero;
import Interfaces.Factory.HeroFactory;

public class OrderFactory implements HeroFactory {
    private static volatile OrderFactory obj = null;
    private OrderFactory() {
        System.out.println("OrderFactory is instantiated.");
    }
    public static OrderFactory getInstance() {
        if (obj == null) {
            synchronized (OrderFactory.class) {
                if (obj == null) {
                    obj = new OrderFactory();
                }
            }
        }
        return obj;
    }

    @Override
    public Hero createHero() {
        return new OrderHero();
    }
}
