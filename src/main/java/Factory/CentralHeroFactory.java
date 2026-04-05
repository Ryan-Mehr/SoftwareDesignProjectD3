package Factory;

import Classes.Heros.Hero;
import Classes.Heros.HeroClass;
import DAO.HeroDAO;
import Interfaces.Factory.HeroFactory;
import Repository.CampaignRepository;

public class CentralHeroFactory {
    private static volatile CentralHeroFactory obj = null;
    private CentralHeroFactory() {
        System.out.println("CentralHeroFactory is instantiated.");
    }
    public static CentralHeroFactory getInstance() {
        if (obj == null) {
            synchronized (CentralHeroFactory.class) {
                if (obj == null) {
                    obj = new CentralHeroFactory();
                }
            }
        }
        return obj;
    }

    public static Hero makeHeroBasedOnClass(String heroClass) {
        Hero heroChosen = null;

        switch(heroClass) {
            case "ORDER":
                heroChosen = OrderFactory.getInstance().createHero();
                heroChosen.setHeroClass(HeroClass.ORDER);
                break;
            case "CHAOS":
                heroChosen = ChaosFactory.getInstance().createHero();
                heroChosen.setHeroClass(HeroClass.CHAOS);
                break;
            case "WARRIOR":
                heroChosen = WarriorFactory.getInstance().createHero();
                heroChosen.setHeroClass(HeroClass.WARRIOR);
                break;
            case "MAGE":
                heroChosen = MageFactory.getInstance().createHero();
                heroChosen.setHeroClass(HeroClass.MAGE);
                break;
        }

        return heroChosen;
    }
}
