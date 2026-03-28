package Factory;

import Classes.Heros.Hero;
import Classes.Heros.HeroClass;
import DAO.HeroDAO;
import Interfaces.Factory.HeroFactory;

public class CentralHeroFactory {
//    private static volatile CentralHeroFactory obj = null;
//    private CentralHeroFactory() {
//        System.out.println("HeroDAO is instantiated.");
//    }
//    public static CentralHeroFactory getInstance() {
//        if (obj == null) {
//            synchronized (CentralHeroFactory.class) {
//                if (obj == null) {
//                    obj = new CentralHeroFactory();
//                }
//            }
//        }
//        return obj;
//    }

    public static OrderFactory orderFactory = new OrderFactory();
    public static ChaosFactory chaosFactory = new ChaosFactory();
    public static WarriorFactory warriorFactory = new WarriorFactory();
    public static MageFactory mageFactory = new MageFactory();

    private CentralHeroFactory() {
        System.out.println("Cannot create CentralHeroFactory Object.");
    }

    public static Hero makeHeroBasedOnClass(String heroClass) {
        Hero heroChosen = null;

        switch(heroClass) {
            case "ORDER":
                heroChosen = orderFactory.createHero();
                heroChosen.setHeroClass(HeroClass.ORDER);
                break;
            case "CHAOS":
                heroChosen = chaosFactory.createHero();
                heroChosen.setHeroClass(HeroClass.CHAOS);
                break;
            case "WARRIOR":
                heroChosen = warriorFactory.createHero();
                heroChosen.setHeroClass(HeroClass.WARRIOR);
                break;
            case "MAGE":
                heroChosen = mageFactory.createHero();
                heroChosen.setHeroClass(HeroClass.MAGE);
                break;
        }

        return heroChosen;
    }
}
